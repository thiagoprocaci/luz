import sys
				

def main():
    scc_file = open(sys.argv[1]) 
    data = scc_file.read()
    count = {}
    for line in data.split('\n'):
        l = line.strip()
        if(count.get(l) is None):
            count[l] = 1
        else:
            count[l] = count[l] + 1
    sum = 0
    for key in count:
        #if(count.get(key) > 1):
        sum = sum + count.get(key)
    print sum

if __name__ == '__main__':
    main()