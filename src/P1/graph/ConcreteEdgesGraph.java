/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements P1.graph.Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //  AF(vertices, edges) = 一个由vertices中的所有顶点和edges中的所有加权边构成的带权有向图
    // Representation invariant:
    //  vertices是一个没有重复顶点的顶点集
    //  edges中的边都是权值为正数且为int类型的有向边，所有边的顶点都在vertices中
    // Safety from rep exposure:
    //   所有数据都为private
    //   vertices为可变数据类型，vertices()方法在返回Set之前使用防御性拷贝
    //   sources()方法和targets()方法在返回Map之前使用防御性拷贝
    
    // TODO constructor
    public ConcreteEdgesGraph() {

    }


    // TODO checkRep
    /**
     * 检测RI是否被保持
     */
    private void checkRep() {
        //vertices为Set类型，无需验证是否有重复顶点
        for (Edge<L> edge: this.edges) {
            assert edge.getWeight() > 0;
            assert this.vertices.contains(edge.getSource());
            assert this.vertices.contains(edge.getTarget());
        }
    }
    
    @Override public boolean add(L vertex) {
        if (this.vertices.contains(vertex)) {
            return false;
        } else {
            this.vertices.add(vertex);
            checkRep();
            return true;
        }
    }
    
    @Override public int set(L source, L target, int weight) {
        //顶点不存在
        if (!vertices.contains(source) || !vertices.contains(target)) {
            if (weight == 0) {
                return 0;
            } else {
                vertices.add(source);
                vertices.add(target);
                edges.add(new Edge<L>(source, target, weight));
                checkRep();
                return 0;
            }
        } else {
            //顶点存在
            Iterator<Edge<L>> iterator = edges.iterator();
            while (iterator.hasNext()) {
                Edge<L> curEdge = iterator.next();
                if (curEdge.getSource().equals(source) && curEdge.getTarget().equals(target)) {
                    //边存在
                    int previousWeight = curEdge.getWeight();
                    iterator.remove();
                    if (weight > 0) {
                        edges.add(new Edge<L>(source, target, weight));
                    }
                    checkRep();
                    return previousWeight;
                }
            }
            //边不存在
            if (weight > 0) {
                edges.add(new Edge<L>(source, target, weight));
            }
            checkRep();
            return 0;
        }

    }
    
    @Override public boolean remove(L vertex) {
        if (!vertices.contains(vertex)) {
            return false;
        } else {
            Iterator<Edge<L>> iterator = edges.iterator();
            while (iterator.hasNext()) {
                Edge<L> curEdge = iterator.next();
                if (curEdge.getSource().equals(vertex) || curEdge.getTarget().equals(vertex)) {
                    iterator.remove();
                }
            }
            vertices.remove(vertex);
            checkRep();
            return true;
        }
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<>(this.vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();
        Iterator<Edge<L>> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge<L> curEdge = iterator.next();
            if (curEdge.getTarget().equals(target)) {
                sources.put(curEdge.getSource(), curEdge.getWeight());
            }
        }
        return new HashMap<L, Integer>(sources);
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();
        Iterator<Edge<L>> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge<L> curEdge = iterator.next();
            if (curEdge.getSource().equals(source)) {
                targets.put(curEdge.getTarget(), curEdge.getWeight());
            }
        }
        return new HashMap<L, Integer>(targets);
    }
    
    // TODO toString()
    /**
     * 返回图的字符串表示
     * @return 图的字符串表示，格式为"vertices{v1,v2,...};edges{source-weight->target,...}"
     */
    @Override
    public String toString() {
        String string = "vertices{";
        int index = vertices.size();
        for (L vertice: vertices) {
            string = string.concat(vertice.toString());
            index--;
            if (index > 0) {
                string = string.concat(",");
            }
        }
        string = string.concat("};edges{");
        for (index = edges.size() - 1; index >= 0; index--) {
            string = string.concat(edges.get(index).toString());
            if (index > 0) {
                string = string.concat(",");
            }
        }
        string = string.concat("}");
        return string;
    }
}

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */

/**
 * 代表带权有向图ConcreteEdgesGraph中的一条带权有向边。
 * Edge为immutable
 */
class Edge<L> {
    
    // TODO fields
    private final L source;
    private final L target;
    private final int weight;
    // Abstraction function:
    //   AF(source, target, weight) = 一条以source为起点，target为终点，权值为weight的有向边
    // Representation invariant:
    //   weight > 0
    // Safety from rep exposure:
    //   所有数据都为private;
    //   表示中的所有类型都是不可变数据类型
    
    // TODO constructor
    /**
     * 创建一条带权有向边
     * @param source    边的起点
     * @param target    边的终点
     * @param weight    边的权值，必须为正整数
     */
    public Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    // TODO checkRep
    /**
     * 检测RI是否被保持
     */
    private void checkRep() {
        assert this.weight > 0;
    }
    
    // TODO methods
    /**
     * 获得带权有向边的起点
     * @return  带权有向边的起点
     */
    public L getSource() {
        return this.source;
    }

    /**
     * 获得带权有向边的终点
     * @return  带权有向边的终点
     */
    public L getTarget() {
        return this.target;
    }

    /**
     * 获得带权有向边的权值
     * @return 带权有向边的权值
     */
    public int getWeight() {
        return this.weight;
    }

    // TODO toString()
    /**
     * 返回边的字符串表示
     * @return 返回边的字符串表示，格式为“起点-权值->终点”
     */
    @Override
    public String toString() {
        return this.source + "-" + this.weight + "->" + this.target;
    }

}
