import java.util.Objects;

class Pair<T, U> 
{
    private final T first;
    private final U second;

    public Pair(T first, U second) 
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
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;
        
        Pair<?, ?> pair = (Pair<?, ?>) o;
        
        return Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.first, this.second);
    }
}