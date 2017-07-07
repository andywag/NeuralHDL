import argparse
import test.CheckDoubleNetwork
import test.CheckSingleNetwork

TEST_SINGLE = 'simple'
TEST_DOUBLE = 'full'

parser = argparse.ArgumentParser(description='Short sample app')
parser.add_argument('-p','--plot',action='store_true',default=False)
parser.add_argument('-t','--test')
parser.add_argument('-s','--stages',type=int,default=1)
parser.add_argument('-th','--threshold',type=float)

def runTest(args) :
    #if (args.test==TEST_DOUBLE) :
    #    test.CheckDoubleNetwork.test(args,2,"full")
    #elif (args.test==TEST_SINGLE) :
    #    test.CheckDoubleNetwork.test(args,1,"simple")
    #else :
    #    print (str(args))
    test.CheckDoubleNetwork.test(args, args.stages, args.test)

if __name__ == "__main__":
    args = parser.parse_args()
    runTest(args)