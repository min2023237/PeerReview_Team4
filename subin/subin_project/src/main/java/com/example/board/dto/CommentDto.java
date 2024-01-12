package com.example.board.dto;
import com.example.board.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @Setter
    private String content;
    @Setter
    private String originPassword;

    public CommentDto(String content, String originPassword) {
        this.content = content;
        this.originPassword = originPassword;
    }

    public static CommentDto fromEntity(Comment entity) {
        CommentDto dto = new CommentDto();
        dto.id = entity.getId();
        dto.content = entity.getContent();
        dto.originPassword = entity.getOriginPassword();
        return dto;
    }
}
