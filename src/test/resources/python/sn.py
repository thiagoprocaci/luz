# -*- coding: utf-8 -*-
import sys
import codecs
import math

def return_zeros(exp, num):
    val = abs(int(exp))
    index = 0
    s = ""
    while index < val:
        if(index == 0):
            s = "0."
        else:
            s = s + "0"
        index = index + 1
    n = str(num)
    n = n.replace(",", "")
    return s + n

def main():	
    nodes_file_path = sys.argv[1]
    nodes_file = codecs.open(nodes_file_path, 'r')
    text = nodes_file.read()      
    
    lines = text.split("\n")
    index = 0
    nodes = {}
    for line in lines:
        nodes[index] = line.strip()
        index = index + 1

    pr_file_path = sys.argv[2]
    pg_file = codecs.open(pr_file_path, 'r')
    
    text = pg_file.read()      
    lines = text.split("\n")
    pg = {}
    for line in lines:
        line_s = line.split(";")
        id = line_s[0].strip()
        value = line_s[1].strip()
        pg[id] =  value
    
    i = 0
    print "id ; Page Rank Dirigido Sem Peso"
    while i < index:
        id = nodes[i]                        
        val = float(pg[id]) * pow(10, 5)
        s = str(val).replace(".",",") 
        print id  + " ; " + s
        #if( 'e' not in pg[id]):
            #s = pg[id].replace(".", ",")
           # print id  + " ; " +  pg[id]
        #else:
            #n = pg[id].split("e")            
            #exp = int(n[1])
            #val = float(n[0]) * pow(10, 5)
            #s = val.replace(".", ",")
            #
        i = i + 1   
    
    
    
  

if __name__ == '__main__':
    main()