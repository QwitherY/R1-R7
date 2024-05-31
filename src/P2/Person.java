package P2;

/**
 * 代表一个人
 * Person为immutable
 */
public class Person {
    private final String name;
    // Abstraction function:
    //  AF(name) = 一个名字为name的人
    // Representation invariant:
    // 无
    // Safety from rep exposure:
    // name为private
    // name为不可变数据类型

    /**
     * 创建一个人
     * @param name  人的名字
     */
    public Person(String name) {
        this.name = name;
    }

    /**
     * 获得人的名字
     * @return  人的名字
     */
    public String getName() {
        return name;
    }

    /**
     * 返回人的字符串表示
     * @return  人的字符串表示，格式为"Person(名字)"
     */
    @Override
    public String toString() {
        return "Person(" + name + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) {
            return false;
        }
        Person p = (Person) o;
        return p.getName().equals(this.name);
    }
}
