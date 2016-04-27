
CREATE TABLE contributor(
	contributor_id serial,
	firstname varchar(32),
	lastname varchar(32),
	constraint contributor_pk primary key(contributor_id)
);

CREATE TABLE location(
	location_id serial,
	latitude numeric(8,6),
	longitude numeric(8,6),
	location_name varchar(64),
	constraint location_pk primary key(location_id)
);

CREATE TABLE source(
	source_id serial,
	source_name varchar(64),
	constraint source_pk primary key(source_id)
);

CREATE TABLE isolate(
	isolate_id serial,
	file_path text not null,
	date_sampled timestamp with time zone,
	constraint isolate_pk primary key(isolate_id)
);

CREATE TABLE sequence(
	sequence_id serial,
	sequence_path text not null,
	secondary_id integer,
	isolate_id integer,
	right_adapt_report text,
	left_adapt_report text,
	assembly_report text,
	date_assembled timestamp with time zone,
	constraint sequence_pk primary key(sequence_id)
);

ALTER TABLE sequence add constraint sequence_isolate_fk foreign key(isolate_id) references isolate(isolate_id);

CREATE TABLE sequence_meta(
	sequence_meta_id serial,
	source_id integer,
	location_id integer,
	contributor_id integer,
	constraint sequence_meta_pk primary key(sequence_meta_id)
);

ALTER TABLE sequence_meta add constraint sequence_meta_location_fk foreign key(location_id) references location(location_id);
ALTER TABLE sequence_meta add constraint sequence_meta_source_fk foreign key(source_id) references source(source_id);
ALTER TABLE sequence_meta add constraint sequence_meta_contributor_fk foreign key(contributor_id) references contributor(contributor_id);

CREATE TABLE sequence_diff(
	sequence_diff_id serial,
	sequence_id integer,
	sequence_meta_id integer,
	constraint sequence_diff_pk primary key(sequence_diff_id)	
);

ALTER TABLE sequence_diff add constraint sequence_diff_sequence_fk foreign key(sequence_id) references sequence(sequence_id);
ALTER TABLE sequence_diff add constraint sequence_diff_sequence_meta_fk foreign key(sequence_meta_id) references sequence_meta(sequence_meta_id);

CREATE TABLE analysis(
	analysis_id serial,
	method varchar(64),
	analysis_date timestamp with time zone not null,
	constraint analysis_pk primary key(analysis_id)
);

CREATE TABLE analysis_sequences(
	analysis_id integer not null,
	sequence_diff_id integer not null,
	UNIQUE(analysis_id, sequence_diff_id)
);

ALTER TABLE analysis_sequences add constraint analysis_sequences_analysis_fk foreign key(analysis_id) references analysis(analysis_id);
ALTER TABLE analysis_sequences add constraint analysis_sequences_sequence_diff_fk foreign key(sequence_diff_id) references sequence_diff(sequence_diff_id);

CREATE TABLE point_meta(
	point_meta_id serial,
	analysis_id integer,
	seq_diff_1 integer,
	seq_diff_2 integer,
	constraint point_meta_pk primary key(point_meta_id)
);

ALTER TABLE point_meta add constraint point_meta_analysis_fk foreign key(analysis_id) references analysis(analysis_id);
ALTER TABLE point_meta add constraint point_meta_sequence_diff1_fk foreign key(seq_diff_1) references sequence_diff(sequence_diff_id);
ALTER TABLE point_meta add constraint point_meta_sequence_diff2_fk foreign key(seq_diff_2) references sequence_diff(sequence_diff_id);

CREATE TABLE point(
	point_id serial,
	posision integer not null,
	point_meta_id integer not null,
	char1 char(1),
	char2 char(1),
	constraint point_pk primary key(point_id)
);

ALTER TABLE point add constraint point_point_meta_fk foreign key(point_meta_id) references point_meta(point_meta_id);

CREATE TABLE segment(
	segment_id serial,
	start integer,
	stop integer,
	subsequence text not null,
	constraint segment_pk primary key(segment_id)
);

CREATE TABLE segment_meta(
	segment_meta_id serial,
	analysis_id integer,
	sequence_diff_id integer,
	importdata varchar(3),
	importedFrom varchar(64),
	constraint segment_meta_pk primary key(segment_meta_id)
);

ALTER TABLE segment_meta add constraint segment_meta_analysis_fk foreign key(analysis_id) references analysis(analysis_id);
ALTER TABLE segment_meta add constraint segment_meta_sequence_diff_fk foreign key(sequence_diff_id) references sequence_diff(sequence_diff_id);

CREATE TABLE segment_type(
	segment_type_id serial,
	segment_id integer not null,
	segment_meta_id integer not null,
	segment_description_id integer not null,
	constraint segment_type_pk primary key(segment_type_id)
);

ALTER TABLE segment_type add constraint segment_type_segment_fk foreign key(segment_id) references segment(segment_id);
ALTER TABLE segment_type add constraint segment_type_segment_meta_fk foreign key(segment_meta_id) references segment_meta(segment_meta_id);

CREATE TABLE gen(
	gen_id serial,
	gen_name varchar(64),
	resistant_to text,
	constraint gen_pk primary key(gen_id)
);

CREATE TABLE segment_description(
	segment_description_id serial,
	gen_id integer,
	description text,
	constraint segment_description_pk primary key(segment_description_id)
);

ALTER TABLE segment_type add constraint segment_type_segment_description_fk foreign key(segment_description_id) references segment_description(segment_description_id);
ALTER TABLE segment_description add constraint segment_description_gen_fk foreign key(gen_id) references gen(gen_id);

CREATE TABLE allele(
	allele_id serial,
	segment_type_id integer,
	secondary_id integer,
	importedFrom varchar(64),
	date_created timestamp with time zone not null,
	constraint allele_pk primary key(allele_id)
);

ALTER TABLE allele add constraint allele_segment_type_fk foreign key(segment_type_id) references segment_type(segment_type_id);

CREATE TABLE profiles(
	profile_id serial,
	complex varchar(64),
	secondary_id integer,
	date_created timestamp with time zone not null,
	constraint profiles_pk primary key(profile_id)
);

CREATE TABLE sets_diff(
	allele_id integer not null,
	profile_id integer not null,
	UNIQUE(allele_id, profile_id)
);

ALTER TABLE sets_diff add constraint sets_diff_allele_fk foreign key(allele_id) references allele(allele_id);
ALTER TABLE sets_diff add constraint sets_diff_profiles_fk foreign key(profile_id) references profiles(profile_id);

create view input_user as 
select c.firstname, c.lastname, s.source_name, l.latitude, l.longitude, l.location_name, i.file_path, i.date_sampled, se.sequence_path, se.right_adapt_report,
se.left_adapt_report, se.assembly_report 
from contributor c, source s, location l, isolate i, sequence se;

create rule add_input as on insert to input_user do instead(
insert into contributor(contributor_id, firstname, lastname) values(default, new.firstname, new.lastname);
insert into source(source_id, source_name) values(default, new.source_name);
insert into location(location_id, latitude, longitude, location_name) values(default, new.latitude, new.longitude, new.location_name);
insert into isolate(isolate_id, file_path, date_sampled) values(default, new.file_path, new.date_sampled);
insert into sequence(sequence_id, sequence_path, isolate_id, right_adapt_report, left_adapt_report, assembly_report) 
	values(default, new.sequence_path, (select last_value from isolate_isolate_id_seq ), new.right_adapt_report, new.left_adapt_report, new.assembly_report);
insert into sequence_meta(sequence_meta_id, source_id, location_id, contributor_id) 
	values(default, (select last_value from source_source_id_seq), (select last_value from location_location_id_seq), (select last_value from contributor_contributor_id_seq)); 
insert into sequence_diff(sequence_diff_id, sequence_id, sequence_meta_id) 
	values(default, (select last_value from sequence_sequence_id_seq), (select last_value from sequence_meta_sequence_meta_id_seq));
);



#create view new_sequence as 
#select c.contributor_id, s.source_id, l.location_id, i.isolate_id, se.sequence_path, se.right_adapt_report, se.left_adapt_report, se.assembly_report, se.date_assembled
#from contributor c, source s, location l, isolate i, sequence se;

#create rule insert_sequence as on insert to new_sequence do instead(
#	insert into sequence(sequence_id, sequence_path, isolate_id, date_assembled, right_adapt_report, left_adapt_report, assembly_report) 
#		values(default, new.seq, new.isolate_id, CURRENT_TIMESTAMP, new.right_adapt_report, new.left_adapt_report, new.assembly_report);
#	insert into sequence_meta(sequence_meta_id, source_id, location_id, contributor_id) values(default, new.source_id, new.location_id, new.contributor_id);
#	insert into sequence_diff(sequence_diff_id, sequence_id, sequence_meta_id) values(default, (select last_value from sequence_sequence_id_seq), 
#		(select last_value from sequence_meta_sequence_meta_id_seq));
#);

#test for insertions of input to view
#insert into input_user(firstname, lastname, latitude, longitude, location_name, source_name, file_path, date_sampled, sequence_path, right_report,
#   left_report, assembly_report)
#	values('Jonas', 'Evensen', '12.345667', '87.654321', 'Hvaler', 'Hval', 'input.txt', '2016/01/01', 'test.txt', 
#	'reportR.txt', 'reportL.txt', 'assembly_report');