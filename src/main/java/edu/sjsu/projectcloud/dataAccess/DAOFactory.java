package edu.sjsu.projectcloud.dataAccess;

/**
 * Created by mallika on 4/30/15.
 */
public class DAOFactory {
    enum Type {SCRUM, WATERFALL, KANBAN};

    public static DAO getInstance(String s) {
        Type t = Type.valueOf(s);

        if (t == Type.WATERFALL) {
            return new WFDAOImpl();
        } else if (t == Type.SCRUM) {
            return new ScrumDAOImpl();
        }

        return null; // don't do this
    }
}
