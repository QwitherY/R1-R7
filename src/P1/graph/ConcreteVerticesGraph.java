/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.time.Period;
import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements P1.graph.Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices) = 一个由vertices中所有顶点及每个顶点的带权有向出边构成的带权有向图
    // Representation invariant:
    //   vertices是一个没有重复的顶点的顶点集
    //   vertices中每个顶点的出边都是权值为正数且为int类型的有向边，边的终点都在vertices中
    // Safety from rep exposure:
    //   vertices为private
    //   vertices()方法在返回Set之前使用防御性拷贝
    //   sources()方法在返回Map之前使用防御性拷贝
    //   targets()方法在返回Map之前使用防御性拷贝
    
    // TODO constructor
    public ConcreteVerticesGraph() {

    }

    // TODO checkRep
    /**
     * 检测RI是否被保持
     */
    private void checkRep() {
        Set<L> labels = new HashSet<>();
        for (Vertex<L> vertex: vertices) {
            assert !labels.contains(vertex.getLabel());
            labels.add(vertex.getLabel());
        }
        for (Vertex<L> vertex: vertices) {
            for (L target: vertex.getTargets().keySet()) {
                assert vertex.getTargets().get(target) > 0;
                assert labels.contains(target);
            }
        }
    }

    @Override public boolean add(L vertex) {
        for (Vertex<L> aVertex: vertices) {
            if (aVertex.getLabel().equals(vertex)) {
                return false;
            }
        }
        vertices.add(new Vertex<L>(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(L source, L target, int weight) {
        boolean edgeExist = false;
        Vertex<L> sourceVertex = null;
        if (weight > 0) {
            this.add(source);
            this.add(target);
        }
        for (Vertex<L> v: vertices) {
            if (v.getLabel().equals(source)) {
                sourceVertex = v;
                for (L t: v.getTargets().keySet()) {
                    if (t.equals(target)) {
                        edgeExist = true;
                        break;
                    }
                }
                break;
            }
        }

        if (weight == 0) {
            if (!edgeExist) {
                return 0;
            } else {
                int previousWeight = sourceVertex.getTargets().get(target);
                sourceVertex.removeTarget(target);
                checkRep();
                return previousWeight;
            }
        } else {
            int previousWeight = sourceVertex.removeTarget(target);
            sourceVertex.addTarget(target, weight);
            checkRep();
            return  previousWeight;
        }
    }
    
    @Override public boolean remove(L vertex) {
        for (Vertex<L> v: vertices) {
            v.removeTarget(vertex);
        }
        Iterator<Vertex<L>> iterator = vertices.iterator();
        while (iterator.hasNext()) {
            Vertex<L> curVertex = iterator.next();
            if (curVertex.getLabel().equals(vertex)) {
                iterator.remove();
                checkRep();
                return true;
            }
        }
        checkRep();
        return false;
    }
    
    @Override public Set<L> vertices() {
        Set<L> verticesSet = new HashSet<>();
        for (Vertex<L> vertex: vertices) {
            verticesSet.add(vertex.getLabel());
        }
        return new HashSet<>(verticesSet);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();
        for (Vertex<L> vertex: vertices) {
            if (vertex.getTargets().containsKey(target)) {
                sources.put(vertex.getLabel(), vertex.getTargets().get(target));
            }
        }
        return new HashMap<>(sources);
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();
        for (Vertex<L> vertex: vertices) {
            if (vertex.getLabel().equals(source)) {
                targets = new HashMap<>(vertex.getTargets());
            }
        }
        return targets;
    }
    
    // TODO toString()
    /**
     * 返回图的字符串表示
     * @return  图的字符串表示，格式为“顶点1{(权值,终点);...}|顶点2{(权值,终点);...}|...”，其中(权值,终点)代表顶点的一条出边，不同顶点之间用"|"分隔
     */
    @Override
    public String toString() {
        String string = "";
        int index = vertices.size();
        for (Vertex<L> vertex: vertices) {
            string = string.concat(vertex.toString());
            index--;
            if (index > 0) {
                string = string.concat("|");
            }
        }
        return string;
    }
}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */

/**
 * 代表ConcreteVerticesGraph中的一个顶点及该顶点的出边
 * Vertex为mutable
 */
class Vertex<L> {
    
    // TODO fields
    private final L label;
    private final Map<L, Integer> targets = new HashMap<>();
    // Abstraction function:
    //   AF(label, targets) = 一个标识为label的顶点，该顶点到任意标识为target的顶点有权值为weight的有向边当且仅当targets.get(target)==weight
    // Representation invariant:
    //   targets中的所有Integer都是值为正整数的权值weight
    // Safety from rep exposure:
    //   所有数据都为private
    //   label为不可变数据类型
    //   targets为可变数据类型，getTargets()方法在返回Map之前使用防御性拷贝
    
    // TODO constructor
    /**
     * 创建一个出度为零的顶点
     * @param label     顶点的标签
     */
    public Vertex(L label) {
        this.label = label;
    }

    // TODO checkRep
    /**
     * 检测RI是否被保持
     */
    private void checkRep() {
        for (L target: targets.keySet()) {
            assert targets.get(target) > 0;
        }
    }
    
    // TODO methods
    /**
     * 获得顶点的标签
     * @return  顶点的标签
     */
    public L getLabel() {
        return label;
    }

    /**
     * 获得此顶点的出边所指向的顶点以及这些边的权值
     * @return  一个map，其键值的集合是顶点的标签集，使得从此顶点到集合中的顶点具有一条带权有向边，且每个键值经map映射得到的值是从此顶点到键值对应顶点的有向边的权重
     */
    public Map<L, Integer> getTargets() {
        return new HashMap<L, Integer>(targets);
    }

    /**
     * 为此顶点添加一条带权有向出边
     * @param target    出边指向的顶点的标签
     * @param weight    出边的权重
     * @return  如果添加的边不存在且weight > 0，返回true；如果weight <= 0或已有指向target的出边，返回false（且不进行任何修改）
     */
    public boolean addTarget(L target, int weight) {
        if (weight <= 0 || targets.containsKey(target)) {
            return false;
        } else {
            targets.put(target, weight);
            checkRep();
            return true;
        }
    }

    /**
     * 删除一条带权有向出边
     * @param target    出边指向的顶点的标签
     * @return  如果边存在，返回此边的权重；否则返回0
     */
    public int removeTarget(L target) {
        if (!targets.containsKey(target)) {
            return 0;
        } else {
            int previousWeight = targets.get(target);
            targets.remove(target);
            checkRep();
            return  previousWeight;
        }
    }

    // TODO toString()
    /**
     * 返回顶点的字符串表示
     * @return  返回顶点的字符串表示，格式为“顶点标签{(权值,终点标签);...}”，其中(权值,终点标签)代表顶点的一条出边
     */
    @Override
    public String toString() {
        String string = label + "{";
        int index = targets.size();
        for (L target: targets.keySet()) {
            string = string .concat("(" + targets.get(target) + "," + target.toString() + ")");
            index--;
            if (index > 0) {
                string = string.concat(";");
            }
        }
        string = string.concat("}");
        return string;
    }
}
