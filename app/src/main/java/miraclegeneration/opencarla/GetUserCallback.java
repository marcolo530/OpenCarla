package miraclegeneration.opencarla;

/**
 * Created by marco on 15/1/2016.
 */
//interface use to specify the return type of the call back function should return after connect the db
public interface  GetUserCallback {
    public abstract void done (User returnedUser);
}
