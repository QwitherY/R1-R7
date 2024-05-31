package P2;

import P1.graph.ConcreteEdgesGraph;
import P1.graph.Graph;

import java.util.*;

public class FriendshipGraph {
    private final Graph<Person> graph = new ConcreteEdgesGraph<>();

    public FriendshipGraph() {

    }
    public void addVertex(Person person) {
        for (Person p: graph.vertices()) {
            if(p.equals(person)) {
                System.out.println(person.getName() + "名称重复");
            }
        }
        graph.add(person);
    }

    public void addEdge(Person person1, Person person2) {
        if (!graph.vertices().contains(person1) || !graph.vertices().contains(person2)) {
            System.out.println("所加边的顶点不存在");
            return;
        }
        if (person1.equals(person2)) {
            System.out.println("边两端为同一个顶点");
            return;
        }
        if (graph.set(person1, person2, 1) != 0) {
            System.out.println("边已存在");
        }
    }
    public int getDistance(Person person1, Person person2) {
        if (!graph.vertices().contains(person1) || !graph.vertices().contains(person2)) {
            System.out.println("顶点不存在");
            return -1;
        }
        if (person1.equals(person2)) {
            return 0;
        }

        Set<Person> visited = new HashSet<>();
        Queue<Person> queue = new LinkedList<>();
        int distance;
        distance = 0;
        queue.offer(person1);
        visited.add(person1);
        while (!queue.isEmpty()) {
            Person curPerson = queue.poll();
            distance++;
            for (Person p: graph.targets(curPerson).keySet()) {
                if (p.equals(person2)) {
                    return distance;
                }
                if (!visited.contains(p)) {
                    visited.add(p);
                    queue.offer(p);
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        System.out.println(graph.getDistance(rachel, ross));
        //should print 1
        System.out.println(graph.getDistance(rachel, ben));
        //should print 2
        System.out.println(graph.getDistance(rachel, rachel));
        //should print 0
        System.out.println(graph.getDistance(rachel, kramer));
        //should print -1
    }
}
