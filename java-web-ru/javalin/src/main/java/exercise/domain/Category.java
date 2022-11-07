package exercise.domain;

import io.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import javax.persistence.CascadeType;

@Entity
public class Category extends Model {

    @Id
    private long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Article> articles;

    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Article> getArticles() {
        return this.articles;
    }
}
