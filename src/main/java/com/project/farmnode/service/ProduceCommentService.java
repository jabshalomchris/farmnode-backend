package com.project.farmnode.service;

import com.project.farmnode.dto.CommentsDto;
import com.project.farmnode.dto.ProduceCommentsDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.CommentMapper;
import com.project.farmnode.mapper.ProduceCommentMapper;
import com.project.farmnode.model.*;
import com.project.farmnode.repo.CommentRepo;
import com.project.farmnode.repo.ProduceCommentRepo;
import com.project.farmnode.repo.ProduceRepo;
import com.project.farmnode.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("ProduceCommentService")
@AllArgsConstructor
public class ProduceCommentService {
    private static final String POST_URL = "";
    private final UserRepo userRepo;
    private final ProduceRepo produceRepo;
    private final ProduceCommentMapper produceCommentMapper;
    private final UserService userService;
    private final ProduceCommentRepo produceCommentRepo;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(ProduceCommentsDto produceCommentsDto, String username) {
        Produce produce = produceRepo.findById(produceCommentsDto.getProduceId())
                .orElseThrow(() -> new ResourceNotFoundException("Produce not found with id: "+produceCommentsDto.getProduceId().toString()));
        ProduceComment produceComment = produceCommentMapper.map(produceCommentsDto, produce, userService.getUser(username));
        produceCommentRepo.save(produceComment);

        /*String message = mailContentBuilder.build(produce.getUser().getName() + " posted a comment on your produce." + POST_URL);
        sendCommentNotification(message, produce.getUser());*/
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getName() + " Commented on your produce", user.getUsername(), message));
    }

    public List<ProduceCommentsDto> getAllCommentsForProduce(Long produceId) {
        Produce produce = produceRepo.findById(produceId)
                .orElseThrow(() -> new ResourceNotFoundException("Produce not found with id: "+produceId.toString()));
        return produceCommentRepo.findByProduce(produce)
                .stream()
                .map(produceCommentMapper::mapToDto).collect(toList());
    }

    public List<ProduceCommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepo.findByUsername(userName);
        if(user==null){
            throw new UsernameNotFoundException("User not found in the database");
        }
        //             .orElseThrow(() -> new UsernameNotFoundException(userName));
        return produceCommentRepo.findAllByUser(user)
                .stream()
                .map(produceCommentMapper::mapToDto)
                .collect(toList());
    }
}
