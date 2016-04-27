import os, sys
from shutil import copyfile

class PythonWrapper:
	def __init__(self, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12):
		workingdir = os.getcwd() + '/'
		print workingdir
		copyfile('/home/jonas/galaxy/tools/straintracer/InputComp.class', workingdir + "/InputComp.class")
		isolateDir = "/home/jonas/galaxy/tools/straintracer/data/isolates/"
		userDir = "%s%s%s" % (isolateDir, arg1, arg2)
		if not os.path.isdir(userDir):
			os.makedirs(userDir)
		os.system('cp %s %s' % (arg8, userDir))
		os.system('cp %s %s' % (arg9, userDir))
		right = arg8.split('_')[1]
		left = arg9.split('_')[1]
		right = "%s/dataset_%s" % (userDir, right)
		left = "%s/dataset_%s" % (userDir, left)
		print right
		print arg8
		os.system('java InputComp %s %s %s %s %s %s %s %s %s %s %s %s' % (arg1, arg2, arg3, arg4, arg5, arg6, arg7, right, left, arg10, arg11, arg12))

		
PythonWrapper(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[8], sys.argv[9], sys.argv[10], sys.argv[11], sys.argv[12])

#arguments:
# 1 = firstname, 2 = lastname, 3 = latitude, 4 = longitude, 5 = location name
# 10 = input text file, 11 = right strand file, 12 = left strand file
