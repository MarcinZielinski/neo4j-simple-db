import org.neo4j.graphdb.RelationshipType;

/**
 * Created by Marcin on 2017-12-21.
 */
public enum Relationships implements RelationshipType{
    VIEWED, RATED, WANT_TO_SEE, NOT_INTERESTED,
    FRIENDS
}
