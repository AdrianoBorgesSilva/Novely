package com.novely.novely.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.novely.novely.domain.Novel;
import com.novely.novely.domain.User;
import com.novely.novely.dto.UserSignUpDTO;
import com.novely.novely.dto.UserUpdateDTO;
import com.novely.novely.exception.FavoritesException;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.SignUpException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NovelService novelService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy NovelService novelService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.novelService = novelService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        
        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        
        if (Boolean.TRUE.equals(user.isDeleted())) {
            throw new ObjectNotFoundException("User not found");
        }

        return user;
    }

    public User findByEmail(String email) {
        
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User not found."));
        
        if (Boolean.TRUE.equals(user.isDeleted())) {
            throw new ObjectNotFoundException("User not found.");
        }

        return user;
    }

    public User createUser(UserSignUpDTO userSignUpDTO) {
        
        if (userRepository.existsByEmail(userSignUpDTO.getEmail())) {
            throw new SignUpException("Email already in use");
        }

        String encryptedPassword = passwordEncoder.encode(userSignUpDTO.getPassword());

        User user = new User(null, userSignUpDTO.getName(), userSignUpDTO.getEmail(), encryptedPassword, "default-avatar.png", LocalDate.now());
    
        return userRepository.save(user);
    }

    public void updateUser(UserUpdateDTO userUpdateDTO, String userId, String authId) {
       
        User user = findById(userId);

        if (!user.getId().equals(authId)) {
            throw new UnauthorizedActionException("You can not update this user");
        }

        if (userUpdateDTO.getName() != null) {
            user.setName(userUpdateDTO.getName());
        
        }
        
        if (userUpdateDTO.getAvatarUrl() != null) {
            user.setAvatarUrl(userUpdateDTO.getAvatarUrl());
        }
        
        userRepository.save(user);
    }

    public void deleteUser(String requesterEmail, String targetUserId) {
        
        User user = findByEmail(requesterEmail);

        if (!user.getId().equals(targetUserId)) {
            throw new UnauthorizedActionException("You can not delete this account");
        }

        delete(targetUserId);
    }

    private void delete(String userId) {
        
        User user = findById(userId);

        user.setName("User deleted");
        user.setEmail("anon_" + UUID.randomUUID() + "@anon.novely");
        user.setPassword("");
        user.setAvatarUrl("deleted_user.png");
        user.getFavorites().clear();
        user.getSuperFavorites().clear();
        user.setDeleted(true);

        userRepository.save(user);
    }

    // Support Methods

    public User addNovelToUser(String userId, String novelId) {
        User user = findById(userId);
        user.getNovelsId().add(novelId);
        return userRepository.save(user);
    }

    public User addCommentToUser(String userId, String commentId) {
        User user = findById(userId);
        user.getCommentsId().add(commentId);
        return userRepository.save(user);
    }

    public User addRatingToUser(String userId, String ratingId) {
        User user = findById(userId);
        user.getRatingsId().add(ratingId);
        return userRepository.save(user);
    }

    public void removeNovelFromUser(String userId, String novelId) {
        User user = findById(userId);
        user.getNovelsId().remove(novelId);
        userRepository.save(user);
    }

    public void removeCommentFromUser(String userId, String commentId) {
        User user = findById(userId);
        user.getCommentsId().remove(commentId);
        userRepository.save(user);
    }

    public void removeRatingFromUser(String userId, String ratingId) {
        User user = findById(userId);
        user.getRatingsId().remove(ratingId);
        userRepository.save(user);
    }

    //Secondary Methods

    public void addFavorite(String userId, String novelId, String authId){
        
        User user = findById(userId);

        if (!user.getId().equals(authId)) {
            throw new UnauthorizedActionException("You can not add favorites in this user");
        }

        Novel novel = novelService.findById(novelId);

        if (user.getFavorites().contains(novelId)) {
            throw new FavoritesException("Novel already in favorites");
        } 
        
        if (user.getFavorites().size() >= 20) {
            throw new FavoritesException("Max number of favorites reached");
        } 
        
        if (user.getSuperFavorites().contains(novelId)) {
            throw new FavoritesException("Novel is in super favorites");
        }

        user.getFavorites().add(novelId);
        novel.setNumberOfFavorites(novel.getNumberOfFavorites() + 1);

        userRepository.save(user);
        novelService.save(novel);
    }

    public void removeFavorite(String userId, String novelId, String authId) {
        
        User user = findById(userId);

        if (!user.getId().equals(authId)) {
            throw new UnauthorizedActionException("You can not remove favorites in this user");
        }

        Novel novel = novelService.findById(novelId);
    
        if (user.getFavorites().remove(novelId)) {
            novel.setNumberOfFavorites(Math.max(0, novel.getNumberOfFavorites() - 1));
            userRepository.save(user);
            novelService.save(novel);
        }
    }

    public void addSuperFavorite(String userId, String novelId, String authId){
        
        User user = findById(userId);
       
        if (!user.getId().equals(authId)) {
            throw new UnauthorizedActionException("You can not add super favorites in this user");
        }

        Novel novel = novelService.findById(novelId);

        if (user.getSuperFavorites().contains(novelId)) {
            throw new FavoritesException("Novel already in super favorites"); 
        }
        
        if (user.getSuperFavorites().size() >= 5) {
            throw new FavoritesException("Max number of super favorites reached");
        } 
        
        if (user.getFavorites().contains(novelId)) {
            throw new FavoritesException("Novel is in favorites");
        }

        user.getSuperFavorites().add(novelId);
        novel.setNumberOfSuperFavorites(novel.getNumberOfSuperFavorites() + 1);

        userRepository.save(user);
        novelService.save(novel);
    }

    public void removeSuperFavorite(String userId, String novelId, String authId) {
        
        User user = findById(userId);
        
        if (!user.getId().equals(authId)) {
            throw new UnauthorizedActionException("You can not remove super favorites in this user");
        }

        Novel novel = novelService.findById(novelId);
    
        if (user.getSuperFavorites().remove(novelId)) {
            novel.setNumberOfSuperFavorites(Math.max(0, novel.getNumberOfSuperFavorites() - 1));
            userRepository.save(user);
            novelService.save(novel);
        }
    }
}