package com.github.devsns.domain.comments.service;

import com.github.devsns.domain.comments.entity.CommentEntity;
import com.github.devsns.domain.comments.repository.CommentRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.user.userEntity.UserEntity;
import com.github.devsns.domain.user.userService.UserService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final PostService postService;

    public CommentServiceImpl(CommentRepository commentRepository, NotificationService notificationService, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.postService = postService;
    }

    public void createComment(Long postId, Long userId, String content) {

        // 게시물 조회
        Post post = postService.getPostById(postId);

        // 댓글 작성자
        UserEntity commenter = userService.getUserById(userId);

        // 게시물 작성자
        UserEntity postAuthor = post.getAuthor();

        CommentEntity comment = new CommentEntity();
        comment.setContent(content);
        comment.setPost(post);
        comment.setCommenter(commenter);
        CommentEntity savedComment = commentRepository.save(comment);


        // 게시물 작성자에게 알림 전송
        notificationService.sendCommentNotification(postAuthor, savedComment);
    }

}


