package test;

/**
 * Created by Developer on 14.03.2016.
 */
class Player
{
    private String name;

    public Player(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (name != null ? !name.equals(player.name) : player.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }
}