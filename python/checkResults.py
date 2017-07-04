import argparse
import test.CheckDoubleNetwork
import test.CheckSingleNetwork

TEST_SINGLE = 'simple'
TEST_DOUBLE = 'full'

parser = argparse.ArgumentParser(description='Short sample app')
parser.add_argument('-p','--plot',action='store_true',default=False)
parser.add_argument('-t','--test',choices={TEST_SINGLE,TEST_DOUBLE},default=TEST_SINGLE)
parser.add_argument('-th','--threshold',type=float)

def runTest(args) :
    if (args.test==TEST_DOUBLE) :
        test.CheckDoubleNetwork.test(args)
    elif (args.test==TEST_SINGLE) :
        test.CheckSingleNetwork.test(args)
    else :
        print (str(args))

if __name__ == "__main__":
    args = parser.parse_args()
    runTest(args)