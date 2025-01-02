/*
 * Eli Bosch, Junior at the University of Arkansas, Computer Science, 1/1/25
 */

import java.util.Objects;

class Pair<T, U> 
{
    private final T first;
    private final U second;

    //Custom class for collision optimization
    public Pair(T first, U second) //Takes two objects and stores them
    {
        this.first = first;
        this.second = second;
    }

    public T getFirst()
    {
        return this.first;
    }

    public U getSecond()
    {
        return this.second;
    }

    @Override
    public boolean equals(Object o) //Checks if two pairs contain the same objects
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;
        
        Pair<?, ?> pair = (Pair<?, ?>) o;
        
        return (Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second) || Objects.equals(this.first, pair.second) && Objects.equals(this.second, pair.first));
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.first, this.second);
    }
}