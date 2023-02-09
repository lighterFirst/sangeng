package com.cheng.vo;

import com.cheng.utils.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowMenuVo implements TreeNode<Long> {

    private Long id;

    private Long parentId;

    private String label;

    private String menuName;

    private List<ShowMenuVo> children;

    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public Long parentId() {
        return this.parentId;
    }

    @Override
    public boolean root() {
        return Objects.equals(this.parentId, 0L);
    }

    @Override
    public void setChildren(List<? extends TreeNode<Long>> children) {
        this.children = (List<ShowMenuVo>) children;
    }
}
