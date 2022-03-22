package com.terralink.terralink_api.domain.article.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.terralink.terralink_api.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"article\"")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Article {

    @Id 
    @Getter
    @GeneratedValue
    private Integer id;

    @Getter @Setter
    @Column(length = 20, nullable = false) 
    private String title;

    @Getter
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Getter @Setter
    @Column(length = 20, nullable = false)
    private String content;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "article_like", 
        joinColumns = { @JoinColumn(name = "article_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    Set<User> likedBy = new HashSet<>();
}
