package com.lostAndFind.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author djk
 */

@Data
public class MenuVo implements Serializable {

    private Long id;
    private String title;
    private String value;
    private Long parentId;
    private String status;
    private List<MenuVo> children;

}
