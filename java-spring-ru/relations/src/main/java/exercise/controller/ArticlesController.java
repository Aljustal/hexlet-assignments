package exercise.controller;

import exercise.model.Article;
import exercise.repository.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;


@RestController
@RequestMapping("/articles")
public class ArticlesController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping(path = "")
    public Iterable<Article> getArticles() {
        return this.articleRepository.findAll();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteArticle(@PathVariable long id) {
        this.articleRepository.deleteById(id);
    }

    // BEGIN
    @PostMapping(path = "")
    public void addNewArticle(@RequestBody Article article) {
        this.articleRepository.save(article);
    }

    @PatchMapping(path = "/{id}")
    public void updateArticle(@PathVariable long id, @RequestBody Article article) {
        article.setId(id);
        this.articleRepository.save(article);
    }

    @GetMapping(path = "/{id}")
    public Article getArticle(@PathVariable long id) {
        return this.articleRepository.findById(id);
    }
    // END
}
