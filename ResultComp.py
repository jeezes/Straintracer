import os, sys
from shutil import copyfile

class ResultComp:
	def __init__(self, inputFile, assemblyFile, resultFile):
		workingdir = os.getcwd() + '/'
		copyfile('/home/jonas/galaxy/tools/straintracer/ResultComp.class', workingdir + "/ResultComp.class")
		copyfile('/home/jonas/galaxy/tools/straintracer/StrainTracerInput.class', workingdir + "/StrainTracerInput.class")
		copyfile('/home/jonas/galaxy/tools/straintracer/PsqlWriter.class', workingdir + "/PsqlWriter.class")
		os.system('java -cp /home/jonas/galaxy/tools/straintracer/postgresql-9.4.1208.jre6.jar:. ResultComp %s %s %s' % (inputFile, assemblyFile, resultFile))

ResultComp(sys.argv[1], sys.argv[2], sys.argv[3])
# 1 = data from user
# 2 = data from assembly
# 3 = data from result