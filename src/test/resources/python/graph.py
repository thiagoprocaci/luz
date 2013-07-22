# -*- coding: utf-8 -*-
import sys
import json
from tarjan import *
import codecs
import random


class Node:
    id, name, indegree, outdegree, pagerank, likes = None, None, None, None, None, None    
    answer_num = None
    
    def __init__(self, id, name):     
        self.id = id
        self.name = name
        self.indegree = 0
        self.outdegree = 0
        self.likes = 0
        self.answer_num = 0
		
    def increase_indegree(self):
        self.indegree = self.indegree + 1        

    def increase_outdegree(self):
        self.outdegree = self.outdegree + 1

    def increase_answer_num(self):
        self.answer_num = self.answer_num + 1

class Edge:
    id, source, dest, weight = None, None, None, None

    def __init__(self, source, dest):
        self.source = source
        self.dest = dest
        self.id = edge_id_generator(source, dest)
        self.weight = 1
        #calcula grau no momento da criacao do edge
        source.increase_outdegree()
        dest.increase_indegree()
        dest.increase_answer_num()

    def increase_weight(self):
        self.weight =  self.weight + 1
        self.dest.increase_answer_num()

class Graph:
    nodes, edges, messages_count, threads_count, scc = None, None, None, None,None
    #classificacao dos nodes de acordo com o bow tie structure
    in_nodes, out_nodes, core_nodes = None, None, None
    messages, root_messages = None, None
    random_nodes = None
    
    def __init__(self, nodes, edges, messages_count, threads_count):
        self.nodes = nodes
        self.edges = edges
        self.messages_count = messages_count
        self.threads_count = threads_count
        self.in_nodes = {}
        self.out_nodes = {}
        self.core_nodes = {}  


    def core_node_percent(self):
        return (len(self.core_nodes) * 100.0) / len(self.nodes)
        
    def in_node_percent(self):
        return (len(self.in_nodes) * 100.0) / len(self.nodes)

    def out_node_percent(self):
        return (len(self.out_nodes) * 100.0) / len(self.nodes)
        
    def others_node_percent(self):
        return (100.0 - self.core_node_percent() - self.in_node_percent() - self.out_node_percent())

class Message:
    content, node, next, previous, id = None, None, None, None, None
    
    def __init__(self, content, node, id):
        self.content = content
        self.node = node
        self.id = id
    
## LOADS ###
		
#metodo auxiliar para gerar os ids dos edges
def edge_id_generator(source, dest):
    id = str(source.id) + "_" + str(dest.id)
    return id
		
#metodo auxiliar para colocar os nodes em um dict
def load_node(nodes, id, name):
    if(nodes.get(id) is None):
        node = Node(id, name)
        nodes[id] = node        
    return nodes

# metodo auxiliar para colocar os edges em um dict
def load_edge(edges, source, dest):
    if(source.id != dest.id):	
        id = edge_id_generator(source, dest)
        if(edges.get(id) is None):
            edge = Edge(source, dest)
            edges[id] = edge			
        else:
            edges.get(id).increase_weight()
    return edges

def load_message(messages, node, content, previous, id_content):
    if(content is not None):        
        if(messages is None):
            messages = {}
        message = Message(content, node, id_content)
        if(previous is not None):
            previous.next = message
            message.previous = previous       
        messages[message.id] = message      
        return message
    return None
 

#carrega os nodes (usuarios) em uma estrutura de dados dado um string json
def load_nodes_edges(json_data): 
    thread_count = 0
    message_count = 0
    nodes = {}
    edges = {}	
    messages = {}
    fb_list = json_data.get('fb')
    for fb_item in fb_list:
        item_data = fb_item.get('data')
        for data in item_data:	
            previous = None
            thread_count = thread_count + 1
            message_count = message_count + 1
            nodes = load_node(nodes, data.get('from').get('id'), data.get('from').get('name'))
            source = nodes.get(data.get('from').get('id'))
            previous = load_message(messages, source, data.get('message'), previous, data.get('id'))
            if((data.get('likes') is not None) and (data.get('likes').get('count') is not None)):
                source.likes = source.likes + int(data.get('likes').get('count'))
            if((data.get('comments') is not None) and (data.get('comments').get('data') is not None)):
                comments_data = data.get('comments').get('data')                
                for comment in comments_data:                    
                    message_count = message_count + 1
                    nodes = load_node(nodes, comment.get('from').get('id'), comment.get('from').get('name'))  
                    dest = nodes.get(comment.get('from').get('id'))
                    edges = load_edge(edges, source, dest)		
                    previous = load_message(messages, dest, comment.get('message'), previous, comment.get('id'))
                    if(comment.get('like_count') is not None):
                        dest.likes = dest.likes + int(comment.get('like_count'))    
    graph = Graph(nodes, edges, message_count, thread_count)
    graph.messages = messages
    load_root_messages(graph)    
    return graph    

#carrega grafo contido em um arquivo	
def load_graph(file_path):
    fb_file = codecs.open(file_path, 'r', 'utf-8-sig')
    text = fb_file.read()    
    json_data = json.loads(text)    
    graph = load_nodes_edges(json_data)
    return graph     
 
def load_root_messages(graph):    
    root_messages = {}
    for key in graph.messages:
        message = graph.messages.get(key)
        if(message.previous is None):
            root_messages[message.id] = message 
    graph.root_messages = root_messages
 
 
 ## CALCS ## 

#algoritmo para agrupar as frequencias dos graus
#retorna dict com key=degree value=freq
def count_degree(graph):
    nodes = graph.nodes
    indegrees = {}
    outdegrees = {}
    for key_node in nodes:
        node = nodes.get(key_node)
        if(indegrees.get(node.indegree) is None):
            indegrees[node.indegree] = 1
        else:
            indegrees[node.indegree] = indegrees[node.indegree] + 1		
        if(outdegrees.get(node.outdegree) is None):
            outdegrees[node.outdegree] = 1
        else:
            outdegrees[node.outdegree] = outdegrees[node.outdegree] + 1
    degrees = {}
    for key in indegrees:
        degrees[key] = [indegrees.get(key), 0]
    for key in outdegrees:
        if(degrees.get(key) is not None):
            degrees.get(key)[1] = outdegrees.get(key)
        else:
            degrees[key] = [0, outdegrees.get(key)]
    return indegrees, outdegrees, degrees

def find_scc(graph):
    g = transform_to_dict(graph)
    components = tarjan(g)    
    scc = {}
    index = 0
    for component in components:
        if((component is not None) and (len(component) > 1)):
            nodes = []
            index = index + 1
            for node_id in component:
                nodes.append(graph.nodes.get(node_id))
            scc[index] = nodes
    graph.scc = scc

#transforma grafo em um dict (matriz de adjacencia)
def transform_to_dict(graph_):
    nodes = graph_.nodes
    edges = graph_.edges
    graph = {}
    for key_node in nodes:
        graph[key_node] = []
    for key_edge in edges:
        edge = edges.get(key_edge)
        graph[edge.source.id].append(edge.dest.id)
    return graph
	
 
def find_in_out_core_nodes(graph):
    #contando componentes core    
    ssc_nodes = {}
    for key in graph.scc:
        components = graph.scc.get(key)
        for node in components:
            if (node.id not in ssc_nodes):                
                ssc_nodes[node.id] = node
    graph.core_nodes = ssc_nodes        
    
    for key in graph.nodes:
        node = graph.nodes.get(key)
        if(node.indegree == 0 and node.outdegree > 0):
            graph.in_nodes[node.id] = node 
        if((node.outdegree) == 0 and (node.indegree) > 0 and (answer_question_from_core(node, graph))):
            graph.out_nodes[node.id] = node   

def answer_question_from_core(node, graph):
    edges = graph.edges
    core_nodes = graph.core_nodes
    for edge_key in edges:
        edge = edges.get(edge_key)
        if((core_nodes.get(edge.source.id) is not None) and (node.id == edge.dest.id) and (core_nodes.get(node.id) is None)):
            return True
    return False


#calcula a relacao do indegree do asker com os indegrees dos repliers
#dict key=asker indegree value=list of replies indegree
def build_rel_asker_replier(graph):
    nodes = graph.nodes
    edges = graph.edges
    asker_replier = {}
    for node_key in nodes:
        asker = nodes.get(node_key)
        for edge_key in edges:            
            edge = edges.get(edge_key)
            if(edge.source.id == asker.id):
                if(asker_replier.get(asker.indegree) is None):
                    asker_replier[asker.indegree] = [edge.dest.indegree]
                elif(edge.dest.indegree not in asker_replier.get(asker.indegree)):
                    asker_replier.get(asker.indegree).append(edge.dest.indegree)
    return asker_replier    

def calc_median_rel_asker_replier(asker_replier):
    asker_replier_median = {}
    for asker_in_degree in asker_replier:
        repliers = asker_replier.get(asker_in_degree)
        repliers_sorted = sorted(repliers)        
        size = len(repliers_sorted)        
        half = int(round(size / 2))
        # par
        if int(size) % 2 == 0:
           half = half - 1
           asker_replier_median[asker_in_degree] = (repliers_sorted[half] + repliers_sorted[half + 1]) / 2           
        else:
            asker_replier_median[asker_in_degree] = repliers_sorted[half]            
    return asker_replier_median
    
## PRINTS ##    

#imprime no padrao gml o grafo (fb.gml)
def print_gml(graph):
    nodes = graph.nodes
    edges = graph.edges
    with codecs.open('fb.gml', 'w', 'utf-8-sig') as gml:
		gml.write("graph \n")
		gml.write("[ \n")
		gml.write("    directed 0 \n")
		for key in nodes:
			gml.write("    node \n")
			gml.write("    [ \n")
			gml.write("      id " + key + " \n")
			gml.write('      label "' + key + '"')
			gml.write("    ] \n")
		for key in edges:
			gml.write("    edge \n")
			gml.write("    [ \n")
			gml.write("      source " + edges.get(key).source.id + " \n")
			gml.write("      target " + edges.get(key).dest.id + " \n")
			gml.write("      weight " + str(edges.get(key).weight) + " \n")
			gml.write("    ] \n")
		gml.write( "] \n")
    
def print_count_degree(indegrees, outdegrees, degrees):
    with codecs.open('indegrees.csv', 'w', 'utf-8-sig') as f:
        f.write("Grau Entrada ; Frequencia  \n")
        for key in indegrees:
            f.write(str(key) + " ; " + str(indegrees.get(key)) + " \n")
    with open('outdegrees.csv', 'w') as f:
        f.write("Grau Saida ; Frequencia  \n")
        for key in outdegrees:
            f.write(str(key) + " ; " + str(outdegrees.get(key))  + " \n")    
    with codecs.open('degrees.csv', 'w', 'utf-8-sig') as f:
        f.write("Grau ; Frequencia Indegree ; Frequencia Outdegree  \n")
        for key in degrees:
			f.write(str(key) + " ; " + str(degrees.get(key)[0]) + " ; " + str(degrees.get(key)[1])  + " \n")
    
def print_node_like(graph):
    with codecs.open('node_likes.csv', 'w', 'utf-8-sig') as f:
        f.write("node ; like count  \n")
        for key in graph.nodes:
            f.write(key + " ; " + str(graph.nodes.get(key).likes) + " \n")    

def print_graph_metadata(graph):
    with codecs.open('graph_metadata.txt', 'w', 'utf-8-sig') as f:
        f.write(" ------ Dados Gerais \n \n")
        f.write("Total de mensagens: " + str(graph.messages_count) + " \n")
        f.write("Total de threads: " + str(graph.threads_count) + " \n")
        f.write("Total de nodes: " + str(len(graph.nodes)) + " \n")
        f.write("Total de edges: " + str(len(graph.edges)) + " \n")
        f.write("Numero de SCC: " + str(len(graph.scc)) + " \n \n")
        
        f.write(" ------ Bow tie structure numeros \n \n")
        f.write("In: " + str(len(graph.in_nodes)) + " nodes \n")
        f.write("Out: " + str(len(graph.out_nodes)) + " nodes \n")
        f.write("Core: " + str(len(graph.core_nodes)) + " nodes \n \n")
        
        f.write(" ------ Bow tie structure percentagem \n \n")
        f.write("In: " + str(graph.in_node_percent()) + " % \n")
        f.write("Out: " + str(graph.out_node_percent()) + " % \n")
        f.write("Core: " + str(graph.core_node_percent()) + " % \n")
        f.write("Others: " + str(graph.others_node_percent()) + " % \n")
        

def print_rel_asker_replier(asker_replier):
    with codecs.open('asker_replier.csv', 'w', 'utf-8-sig') as f:
        f.write("asker indegree ; replier indegree  \n")
        for asker_in_degree in asker_replier:
            repliers = asker_replier.get(asker_in_degree)
            for replier_in_degree in repliers:
                f.write(str(asker_in_degree) + " ; " + str(replier_in_degree) + " \n")


def print_rel_median_asker_replier(asker_replier_median):
    keys = asker_replier_median.keys()
    keys = sorted(keys)
    with codecs.open('asker_replier_median.csv', 'w', 'utf-8-sig') as f:
        f.write("asker indegree ; replier median indegree  \n")
        for asker_in_degree in keys:
            f.write(str(asker_in_degree) + " ; " + str(asker_replier_median.get(asker_in_degree)) + " \n")          
       
def print_nodes_messages(graph):    
    top_messages = graph.root_messages
    with codecs.open('mensagens.html', 'w', 'utf-8-sig') as f: 
        f.write("<html><head></head><body>")
        for key in top_messages:
            if(top_messages.get(key).node.id in graph.random_nodes):
                f.write('<table><tr style="background-color:red">')
            else:
                f.write('<table><tr style="background-color:00CC99">')
            f.write('<td>' + top_messages.get(key).node.id +  '</td>')
            f.write('<td><pre>' + escape_html(top_messages.get(key).content) +  '</pre></td>')
            f.write('</tr>')
            next = top_messages.get(key).next
            while(next is not None):
                if(next.node.id in graph.random_nodes):
                    f.write('<tr style="background-color:red">')
                else:
                    f.write('<tr style="background-color:99FF66">')                
                f.write('<td>' + next.node.id +  '</td>')
                f.write('<td><pre>' + escape_html(next.content) +  '</pre></td>')
                f.write('</tr>')
                next = next.next
            f.write('</table>')
            f.write('<br /> <br />')            
        f.write("</body></html>")

def print_HR_analysis_file(graph):
    node_messages = {}
    for key in graph.messages:
        node = graph.messages.get(key).node
        if(node.id in graph.random_nodes):
            if(node_messages.get(node.id) is None):
                node_messages[node.id] = []
            node_messages[node.id].append(graph.messages.get(key).content)
        
    with codecs.open('mensagens_random.txt', 'w', 'utf-8-sig') as f: 
        for key in node_messages:
            f.write(key +  ' \n \n ')            
            for msg in node_messages.get(key):                
                    f.write(msg + '\n \n')
            f.write('****************************************** \n \n')

    with codecs.open('HR.csv', 'w', 'utf-8-sig') as f: 
        f.write('node ; HR ; Indegree ; Answer Num ; Like \n')
        for key in node_messages:
            node = graph.nodes.get(key)
            f.write(key +  ' ;  -  ;' + str(node.indegree) + " ; " + str(node.answer_num) + " ; " + str(node.likes) +  "   \n")

def escape_html(text):
    text = text.replace('&', '&amp;')
    text = text.replace('"', '&quot;')
    text = text.replace("'", '&#39;')
    text = text.replace(">", '&gt;')
    text = text.replace("<", '&lt;')
    return text

def define_random_nodes(graph):
    random_nodes = {}
    number_nodes = 50
    index = 0
    all_node_keys = graph.nodes.keys()
    size = len(all_node_keys)
    while index < 50 and 50 < size:        
        node_key = random.choice(all_node_keys)
        if(random_nodes.get(node_key) is None):
            index += 1
            random_nodes[node_key] = graph.nodes.get(node_key)   
    graph.random_nodes = random_nodes    
    
    
#fazer algoritmo para calcular o z-score
#implementar o page rank


def main():	
    file_path = sys.argv[1]
    #load e calculos
    print "loading graph ...."
    graph = load_graph(file_path)        
    print "defining random nodes ...."
    define_random_nodes(graph)    
    print "counting the degrees ...."
    indegrees, outdegrees, degrees = count_degree(graph) 
    print "find scc ...."
    find_scc(graph)    
    print "finding in, out, core components ...."
    find_in_out_core_nodes(graph)
    print "building indegree relation asker-replier ...."
    asker_replier = build_rel_asker_replier(graph)
    print "calculating median indegree asker-replier"
    asker_replier_median = calc_median_rel_asker_replier(asker_replier)
    
    #print dos dados
    print "printing gml ...."
    print_gml(graph)   
    print "printing degrees ...."
    print_count_degree(indegrees, outdegrees, degrees)    
    print "printing node likes ...."
    print_node_like(graph)
    print "printing graph metadata ...."
    print_graph_metadata(graph)    
    print "printing relation asker-replier ...."
    print_rel_asker_replier(asker_replier)
    print "printing median indegree asker-replier ...."
    print_rel_median_asker_replier(asker_replier_median)
    print "printing all messages ...."    
    print_nodes_messages(graph)
    print "printing HR analysis file"
    print_HR_analysis_file(graph)
    
    print "end"
  

if __name__ == '__main__':
    main()