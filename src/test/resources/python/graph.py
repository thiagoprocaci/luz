import sys
import json
from tarjan import *

class Node:
    id, name, indegree, outdegree, pagerank, likes = None, None, None, None, None, None
 
    def __init__(self, id, name):     
        self.id = id
        self.name = name
        self.indegree = 0
        self.outdegree = 0
        self.likes = 0
		
    def increase_indegree(self):
        self.indegree = self.indegree + 1

    def increase_outdegree(self):
        self.outdegree = self.outdegree + 1

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

    def increase_weight(self):
        self.weight =  self.weight + 1

class Graph:
    nodes, edges, messages, threads, scc = None, None, None, None,None
    #classificacao dos nodes de acordo com o bow tie structure
    in_nodes, out_nodes, core_nodes = None, None, None
    
    def __init__(self, nodes, edges, messages, threads):
        self.nodes = nodes
        self.edges = edges
        self.messages = messages
        self.threads = threads
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

#carrega os nodes (usuarios) em uma estrutura de dados dado um string json
def load_nodes_edges(json_data): 
    thread_count = 0
    message_count = 0
    nodes = {}
    edges = {}	
    fb_list = json_data.get('fb')
    for fb_item in fb_list:
        item_data = fb_item.get('data')
        for data in item_data:		                
            thread_count = thread_count + 1
            message_count = message_count + 1
            nodes = load_node(nodes, data.get('from').get('id'), data.get('from').get('name'))
            source = nodes.get(data.get('from').get('id'))
            if((data.get('likes') is not None) and (data.get('likes').get('count') is not None)):
                source.likes = source.likes + int(data.get('likes').get('count'))
            if((data.get('comments') is not None) and (data.get('comments').get('data') is not None)):
                comments_data = data.get('comments').get('data')                
                for comment in comments_data:                    
                    message_count = message_count + 1
                    nodes = load_node(nodes, comment.get('from').get('id'), comment.get('from').get('name'))  
                    dest = nodes.get(comment.get('from').get('id'))
                    edges = load_edge(edges, source, dest)		
                    if(comment.get('like_count') is not None):
                        dest.likes = dest.likes + int(comment.get('like_count'))    
    graph = Graph(nodes, edges, message_count, thread_count)
    return graph    

#carrega grafo contido em um arquivo	
def load_graph(file_path):
    fb_file = open(file_path)    
    text = fb_file.read()
    #utf8_text = text.encode('utf-8')
    json_data = json.loads(text)
    graph = load_nodes_edges(json_data)
    return graph     
 
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

    
## PRINTS ##    

#imprime no padrao gml o grafo (fb.gml)
def print_gml(graph):
    nodes = graph.nodes
    edges = graph.edges
    with open('fb.gml', 'w') as gml:
		gml.write("graph \n")
		gml.write("[ \n")
		gml.write("    directed 0 \n")
		for key in nodes:
			gml.write("    node \n")
			gml.write("    [ \n")
			gml.write("      id " + key + " \n")
			#print '      label "' + nodes.get(key).name + '"'
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
    with open('indegrees.csv', 'w') as f:
        f.write("Grau Entrada ; Frequencia  \n")
        for key in indegrees:
            f.write(str(key) + " ; " + str(indegrees.get(key)) + " \n")
    with open('outdegrees.csv', 'w') as f:
        f.write("Grau Saida ; Frequencia  \n")
        for key in outdegrees:
            f.write(str(key) + " ; " + str(outdegrees.get(key))  + " \n")    
    with open('degrees.csv', 'w') as f:
        f.write("Grau ; Frequencia Indegree ; Frequencia Outdegree  \n")
        for key in degrees:
			f.write(str(key) + " ; " + str(degrees.get(key)[0]) + " ; " + str(degrees.get(key)[1])  + " \n")
    
def print_node_like(graph):
    with open('node_likes.csv', 'w') as f:
        for key in graph.nodes:
            f.write(key + " ; " + str(graph.nodes.get(key).likes) + " \n")    

def print_graph_metadata(graph):
    with open('graph_metadata.txt', 'w') as f:
        f.write(" ------ Dados Gerais \n \n")
        f.write("Total de mensagens: " + str(graph.messages) + " \n")
        f.write("Total de threads: " + str(graph.threads) + " \n")
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
    with open('asker_replier.csv', 'w') as f:
        f.write("asker indegree ; replier indegree  \n")
        for asker_in_degree in asker_replier:
            repliers = asker_replier.get(asker_in_degree)
            for replier_in_degree in repliers:
                f.write(str(asker_in_degree) + " ; " + str(replier_in_degree) + " \n")
        
    
       
#carregar os comentarios	
#fazer algoritmo para calcular o z-score
#implementar o page rank


def main():	
    file_path = sys.argv[1]
    #load e calculos
    print "loading graph ...."
    graph = load_graph(file_path)
    print "counting the degrees ...."
    indegrees, outdegrees, degrees = count_degree(graph) 
    print "find scc"
    find_scc(graph)    
    print "finding in, out, core components ...."
    find_in_out_core_nodes(graph)
    print "building relation asker-replier ...."
    asker_replier = build_rel_asker_replier(graph)
    
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
    
    print "end"
  

if __name__ == '__main__':
    main()