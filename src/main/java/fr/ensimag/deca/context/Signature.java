package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }

    public Type paramNumber(int n) {
        return args.get(n);
    }

    public int size() {
        return args.size();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Signature){
            Signature s = (Signature)o;

            return args.equals(s.args);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return args.hashCode();
    }

    @Override
    public String toString(){
        return args.toString();
    }
}