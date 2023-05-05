package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.model.request.PickAddRequest;
import com.lostAndFind.project.model.request.PickUpdateRequest;
import com.lostAndFind.project.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
@RestController
@RequestMapping("/tag")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"})
public class TagController {

    @Autowired
    TagService tagService;

    /**
     * 新增标签
     *
     * @param tag 标签
     * @return
     */
    @PostMapping("/add")
    @LogOperation("新增")
    public BaseResponse<String> save(@RequestBody Tag tag) {
        tagService.save(tag);
        return ResultUtils.success("新增标签成功");
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/delete")
    @LogOperation("删除")
    public BaseResponse<String> delete(@RequestParam List<Long> ids) {
        tagService.removeWithPick(ids);
        return ResultUtils.success("删除成功");
    }

    /**
     * 修改标签
     */
    @PostMapping("/update")
    @LogOperation("修改")
    public BaseResponse<Boolean> update(@RequestBody Tag tag) {
        tagService.updateById(tag);
        return ResultUtils.success(true);
    }


    /**
     * 根据id查询拾取物品
     */
    @GetMapping("/{id}")
    @LogOperation("查询")
    public BaseResponse<Tag> get(@PathVariable("id") Long id) {
        Tag tag = tagService.getById(id);
        return ResultUtils.success(tag);
    }


    @GetMapping("/listOne")
    public BaseResponse<List<Tag>> listOne() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getParentId,0);
        List<Tag> list = tagService.list(wrapper);
        return ResultUtils.success(list);
    }

    @GetMapping("/listTwo")
    public BaseResponse<List<Tag>> listTwo(Long parentId) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getParentId,parentId);
        List<Tag> list = tagService.list(wrapper);
        return ResultUtils.success(list);
    }

}

