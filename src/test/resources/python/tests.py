import unittest
from graph import *

class GraphTestCase(unittest.TestCase):
#python -m unittest discover
    def test_node_class(self):
        node = Node(1,'name')
        self.assertEqual(1, node.id)
        self.assertEqual('name', node.name)
        self.assertEqual(0, node.indegree)
        self.assertEqual(0, node.outdegree)
        node.increase_indegree()
        node.increase_indegree()
        self.assertEqual(2, node.indegree)
        node.increase_outdegree()
        node.increase_outdegree()
        node.increase_outdegree()        
        self.assertEqual(3, node.outdegree)
        
    def test_graph_class(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')        
        edge = Edge(source, dest)
        nodes = {source.id:source, dest.id:dest}
        edges = {edge.id:edge}
        graph = Graph(nodes, edges, 1, 2)
        self.assertEqual(2, len(graph.nodes))
        self.assertEqual(1, len(graph.edges))
        self.assertEqual(1, graph.messages)
        self.assertEqual(2, graph.threads)
        self.assertTrue(source.id in graph.nodes)
        self.assertTrue(dest.id in graph.nodes)
        self.assertFalse(3 in graph.nodes)
		
    def test_edge_class(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')
        edge = Edge(source, dest)
        self.assertEqual(edge.source.id, source.id)
        self.assertEqual(edge.dest.id, dest.id)
        self.assertEqual(1, edge.weight)		
        edge.increase_weight()		
        edge.increase_weight()
        self.assertEqual(3, edge.weight)	
        self.assertEqual(edge_id_generator(source, dest), edge.id)		

    def test_edge_id_generator(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')
        self.assertEqual('1_2',edge_id_generator(source, dest))		

    def test_load_node(self):
        nodes = {}
        nodes = load_node(nodes, 1 , 'name')
        nodes = load_node(nodes, 1 , 'name')
        self.assertEqual(1, nodes.get(1).id)
        self.assertEqual('name', nodes.get(1).name)
        self.assertEqual(1, len(nodes))
        nodes = load_node(nodes, 2 , 'name_2')
        self.assertEqual(2, nodes.get(2).id)
        self.assertEqual('name_2', nodes.get(2).name)
        self.assertEqual(2, len(nodes))
		
    def test_load_edge(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')
        id = edge_id_generator(source, dest)
        edges = {}
        edges = load_edge(edges, source, source)
        self.assertEqual(0, len(edges))
        edges = load_edge(edges, source, dest)
        self.assertEqual(1, len(edges))
        self.assertEqual(source.id, edges.get(id).source.id)
        self.assertEqual(dest.id, edges.get(id).dest.id)		
        self.assertEqual(1, edges.get(id).weight)
        edges = load_edge(edges, source, dest)
        self.assertEqual(1, len(edges))
        self.assertEqual(2, edges.get(id).weight)
		
    def test_load_nodes_edges(self):
        file_path = "fb_feed.txt"
        graph = load_graph(file_path)
        self.assertEqual(12, len(graph.nodes))
        self.assertEqual(46, len(graph.edges))	
		#melhorar esse teste

    def test_calc_degree(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')        
        edge = Edge(source, dest)
        nodes = {source.id:source, dest.id:dest}
        edges = {edge.id:edge}
        graph = Graph(nodes, edges, 0 , 0)        
        self.assertEqual(1, source.outdegree)
        self.assertEqual(0, source.indegree)
        self.assertEqual(0, dest.outdegree)
        self.assertEqual(1, dest.indegree)

    def test_count_degree(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')   
        dest_ = Node(3, 'name_3')		
        edge = Edge(source, dest)
        edge_ = Edge(source, dest_)
        nodes = {source.id:source, dest.id:dest, dest_.id:dest_}
        edges = {edge.id:edge, edge_.id:edge_}
        graph = Graph(nodes, edges, 0 , 0)        
        indegrees , outdegrees, degrees = count_degree(graph)
        self.assertEqual(2, len(outdegrees))
        self.assertEqual(2, len(indegrees))
        self.assertEqual(2, outdegrees.get(0))
        self.assertEqual(1, outdegrees.get(2))
        self.assertEqual(2, indegrees.get(1))
        self.assertEqual(1, indegrees.get(0))
        self.assertEqual(3, len(degrees))
        self.assertEqual(1, degrees.get(0)[0])
        self.assertEqual(2,degrees.get(0)[1])
        self.assertEqual(2,degrees.get(1)[0])
        self.assertEqual(0,degrees.get(1)[1])
        self.assertEqual(0,degrees.get(2)[0])
        self.assertEqual(1,degrees.get(2)[1])
		
    def test_transform_to_dict(self):
        source = Node(1,'name_1')
        dest = Node(2,'name_2')   
        dest_ = Node(3, 'name_3')		
        edge = Edge(source, dest)
        edge_ = Edge(source, dest_)
        nodes = {source.id:source, dest.id:dest, dest_.id:dest_}
        edges = {edge.id:edge, edge_.id:edge_}
        graph = Graph(nodes, edges, 0 , 0)
        dict = transform_to_dict(graph)
        self.assertEqual(3, len(dict))      
        self.assertEqual(2, len(dict.get(source.id)))		
        self.assertEqual(0, len(dict.get(dest.id)))
        self.assertEqual(0, len(dict.get(dest_.id)))
        self.assertTrue(dest.id in dict.get(source.id))   
        self.assertTrue(dest_.id in dict.get(source.id))   
        self.assertFalse(source.id in dict.get(source.id))  
        
    def test_find_in_out_core_nodes(self):
        node_1 = Node(1, 'name_1')
        node_2 = Node(2, 'name_2')   
        node_3 = Node(3, 'name_3')
        node_4 = Node(4, 'name_4')
        node_5 = Node(5, 'name_5')
        node_6 = Node(6, 'name_6')   
        node_7 = Node(7, 'name_7')
        
        # so perguntam (in components)
        edge_4 = Edge(node_1, node_4)
        edge_5 = Edge(node_2, node_4)
        
        # so responde do core (out components)
        edge_6 = Edge(node_5, node_3)        
        
        
        
        edge_1 = Edge(node_5, node_6)
        edge_2 = Edge(node_6, node_7)
        edge_3 = Edge(node_7, node_5)        
           
        
        nodes = {node_1.id:node_1, node_2.id:node_2, node_3.id:node_3, node_4.id:node_4, node_5.id:node_5, node_6.id:node_6, node_7.id:node_7} 
        edges = {edge_1.id:edge_1, edge_2.id:edge_2, edge_3.id:edge_3, edge_4.id:edge_4, edge_5.id:edge_5, edge_6.id:edge_6}
        
        graph = Graph(nodes, edges, 0, 0)
        find_scc(graph)
        find_in_out_core_nodes(graph)
        
        self.assertEqual(2, len(graph.in_nodes))
        self.assertEqual(1, len(graph.out_nodes))
        self.assertEqual(3, len(graph.core_nodes))
        self.assertTrue(node_1.id in graph.in_nodes)
        self.assertTrue(node_2.id in graph.in_nodes)
        self.assertTrue(node_3.id in graph.out_nodes)

    def test_find_scc(self):
        node_1 = Node(1, 'name_1')
        node_2 = Node(2, 'name_2')   
        node_3 = Node(3, 'name_3')
        node_4 = Node(4, 'name_4')
        node_5 = Node(4, 'name_5')
        
        edge_1 = Edge(node_1, node_2)
        edge_2 = Edge(node_2, node_1)
        edge_3 = Edge(node_1, node_3)
        edge_4 = Edge(node_3, node_2)
        edge_5 = Edge(node_4, node_5)
        edge_6 = Edge(node_5, node_1)
        
        nodes = {node_1.id:node_1, node_2.id:node_2, node_3.id:node_3, node_4.id:node_4, node_5.id:node_5}
        edges = {edge_1.id:edge_1, edge_2.id:edge_2, edge_3.id:edge_3, edge_4.id:edge_4, edge_5.id:edge_5, edge_6.id:edge_6}
        
        graph = Graph(nodes, edges, 0, 0)
        find_scc(graph)
        self.assertEqual(1, len(graph.scc))
        self.assertTrue(node_1 in graph.scc.get(1))
        self.assertTrue(node_2 in graph.scc.get(1))
        self.assertTrue(node_3 in graph.scc.get(1))
        
    def test_answer_question_from_core(self):
        node_1 = Node(1, 'name_1')
        node_2 = Node(2, 'name_2')   
        node_3 = Node(3, 'name_3')
        node_4 = Node(4, 'name_4')        
        node_5 = Node(5, 'name_5')
        node_6 = Node(6, 'name_6')   
        node_7 = Node(7, 'name_7')
        
        edge_4 = Edge(node_5, node_1)
        edge_5 = Edge(node_2, node_4)       
        edge_6 = Edge(node_4, node_3)                
  
        edge_1 = Edge(node_5, node_6)
        edge_2 = Edge(node_6, node_7)
        edge_3 = Edge(node_7, node_5)           
        
           
        
        nodes = {node_1.id:node_1, node_2.id:node_2, node_3.id:node_3, node_4.id:node_4, node_5.id:node_5, node_6.id:node_6, node_7.id:node_7} 
        edges = {edge_1.id:edge_1, edge_2.id:edge_2, edge_3.id:edge_3, edge_4.id:edge_4, edge_5.id:edge_5, edge_6.id:edge_6}
        
        graph = Graph(nodes, edges, 0, 0)        
        #scc nodes
        graph.core_nodes = {node_5.id:node_5, node_6.id:node_6, node_7.id:node_7}
        
        self.assertTrue(answer_question_from_core(node_1, graph))
        self.assertFalse(answer_question_from_core(node_2, graph))
        self.assertFalse(answer_question_from_core(node_3, graph))
        self.assertFalse(answer_question_from_core(node_4, graph))
        self.assertFalse(answer_question_from_core(node_5, graph))
        self.assertFalse(answer_question_from_core(node_6, graph))
        self.assertFalse(answer_question_from_core(node_7, graph))
        
        
        
        
        
		
        
		
		
def main():
    unittest.main()