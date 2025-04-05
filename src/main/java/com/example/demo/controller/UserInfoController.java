package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UserAddRequest;
import com.example.demo.dto.UserSearchRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserInfoService;

/**
 * ユーザー情報 Controller
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {

    /**
     * ユーザー情報 Service
     */
    @Autowired
    private UserInfoService userInfoService;

    /**
     * ユーザー情報一覧画面を表示
     * * @param model Model
     * @return ユーザー情報一覧画面
     */
    @GetMapping("/list")
    public String displayList(Model model) {
        List<UserInfo> userList = userInfoService.findAll();
        model.addAttribute("userlist", userList);
        model.addAttribute("userSearchRequest", new UserSearchRequest());
        return "user/search";
    }

    /**
     * ユーザー新規登録画面を表示
     * * @param model Model
     * @return ユーザー情報一覧画面
     */
    @GetMapping("/add")
    public String displayAdd(Model model) {
        model.addAttribute("userAddRequest", new UserAddRequest());
        return "user/add";
    }

    /**
     * ユーザー編集画面を表示
     * * @param id    ユーザーID
     * @param model Model
     * @return ユーザー編集画面
     */
    @GetMapping("/{id}/edit")
    public String displayEdit(@PathVariable Long id, Model model) {
        UserInfo user = userInfoService.findById(id);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(user.getId());
        userUpdateRequest.setName(user.getName());
        userUpdateRequest.setPhone(user.getPhone());
        userUpdateRequest.setAddress(user.getAddress());
        model.addAttribute("userUpdateRequest", userUpdateRequest);
        return "user/edit";
    }

    /**
     * ユーザー情報検索
     * * @param userSearchRequest リクエストデータ
     * @param model             Model
     * @return ユーザー情報一覧画面
     */
    @PostMapping("/search")
    public String search(@Validated @ModelAttribute UserSearchRequest userSearchRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            List<UserInfo> userList = new ArrayList<>(); //空のリスト作成
            model.addAttribute("userlist", userList);
            return "user/search";
        }

        List<UserInfo> userList = userInfoService.search(userSearchRequest);
        model.addAttribute("userlist", userList);
        return "user/search";
    }

    /**
     * ユーザー情報削除（論理削除）
     * * @param id    ユーザーID
     * @param model Model
     * @return ユーザー情報一覧画面
     */
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userInfoService.delete(id);
        redirectAttributes.addFlashAttribute("message", "ユーザー情報を削除しました。");
        return "redirect:/user/list";
    }

    /**
     * ユーザー新規登録
     * * @param userRequest リクエストデータ
     * @param model       Model
     * @return ユーザー情報一覧画面
     */
    @PostMapping("/create")
    public String create(@Validated @ModelAttribute UserAddRequest userRequest, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // 入力チェックエラーの場合
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "user/add";
        }
        // ユーザー情報の登録
        userInfoService.save(userRequest);
        redirectAttributes.addFlashAttribute("message", "ユーザー情報を登録しました。");
        return "redirect:/user/list";
    }

    /**
     * ユーザー更新
     * * @param userRequest リクエストデータ
     * @param model       Model
     * @return ユーザー情報詳細画面
     */
    @PostMapping("/update")
    public String update(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "user/edit";
        }
        // ユーザー情報の更新
        userInfoService.update(userUpdateRequest);
        redirectAttributes.addFlashAttribute("message", "ユーザー情報を更新しました。");
        return "redirect:/user/list";
    }
}