# -*- coding: utf-8 -*-
import sys


def main():
    pg_file = open(sys.argv[1])
    pg_text = pg_file.read()
    lines = pg_text.split("\n")
    index = 0
    for line in lines:
        if(index != 0):        
            values = line.split(";")
            node_id_string = values[0]
            page_rank_string = values[1]
            node_values = node_id_string.split("E")
            #print node_values
            #node_id = int(node_values[0].replace(".", ""))
            #node_pow = int(node_values[1])
            if(len(node_values) == 1):
                print node_values
            #print str(node_values[0].replace(".", ""))        
            #print str(node_values[1])
            page_rank_values = page_rank_string.split("E")
        index += 1
        

if __name__ == '__main__':
    main()