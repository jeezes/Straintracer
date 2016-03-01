import os, sys, time
from shutil import copyfile

class AssemblyComp:
	def __init__(self, sequencefile, assemblyreport, adaptright, adaptleft, sequence, report):
		workingdir = os.getcwd() + '/'
		copyfile('/home/jonas/galaxy/tools/straintracer/AssemblyComp.class', workingdir + "/AssemblyComp.class")
		reportDir = "/home/jonas/galaxy/tools/straintracer/data/reports/"
		t = time.strftime("%Y/%m/%d")
		reportDir = "%s%s" % (reportDir, t)
		if not os.path.isdir(reportDir):
			os.makedirs(reportDir)
		os.system('cp %s %s' % (assemblyreport, reportDir))
		os.system('cp %s %s' % (adaptright, reportDir))
		os.system('cp %s %s' % (adaptleft, reportDir))
		assRep = assemblyreport.split('/')[-1]
		adRRep = adaptright.split('/')[-1]
		adLRep = adaptleft.split('/')[-1]
		os.system('java AssemblyComp %s %s %s %s %s %s' % (sequencefile, assRep, adRRep, adLRep, sequence, report))

AssemblyComp(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6])
