import os, sys
from shutil import copyfile

# Adds an allele to the DB
# Inputfile: (single or multiple) fasta file with gen and the allele sequence
# Source: Site or origin from where the alleles came from. PS: Must match the same name when adding ST data!!!
class AddAllele:
	def __init__(self, inputfile, stfile, source):
		workingdir = os.getcwd() + '/'
		copyfile('/home/jonas/galaxy/tools/straintracer/GetMlst.class', workingdir + "/GetMlst.class")
		os.system('java GetMlst -i %s %s' % (inputfile, source))
		os.system('java GetMlst -ist %s %s' % (stfile, source))

AddAllele(sys.argv[1], sys.argv[2], sys.argv[3])

