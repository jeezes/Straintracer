import os, sys
from shutil import copyfile

class GetAlleles:
	def __init__(self, option, stFile, alleles):
		workingdir = os.getcwd() + '/'
		galaxydir = workingdir.split('/galaxy')[0]
		print workingdir
		print galaxydir
		copyfile(galaxydir + '/galaxy/tools/straintracer/GetMlst.class', workingdir + "/GetMlst.class")
		copyfile(galaxydir + '/galaxy/tools/straintracer/PsqlWriter.class', workingdir + "/PsqlWriter.class")
		#copyfile(galaxydir + '/galaxy/tools/straintracer/postgresql-9.4.1208.jre6.jar', workingdir + "/postgresql-9.4.1208.jre6.jar") #Funker ikke
		#postgresql-9.4.1208.jre6.jar
		#os.system('cp -r %s/tools/straintracer/*.class %s/' % (galaxydir, workingdir))
		os.system('java -cp /home/jonas/galaxy/tools/straintracer/postgresql-9.4.1208.jre6.jar:. GetMlst %s %s %s' % (option, stFile, alleles))

GetAlleles(sys.argv[1], sys.argv[2], sys.argv[3])