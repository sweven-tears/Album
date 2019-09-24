package luoluna.sweven.album.broadcast;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.util.Verifier;

public class A {
    private int id;
    private String name;

    @Override
    public String toString() {
        return "A{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] a) {
        A z = new A();
        z.id=1;
        z.name="sdf";
        A b=new A();
        b.name="sdf";
        b.id=1;
        List<A> list=new ArrayList<>();
        list.add(z);
        System.out.println(Verifier.contains(list,b));
    }
}
