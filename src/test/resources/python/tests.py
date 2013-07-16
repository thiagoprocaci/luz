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
        calc_degree(graph)
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
        calc_degree(graph)
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
		
        
		
		
def main():
    unittest.main()