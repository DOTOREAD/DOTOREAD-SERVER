package likelion.dotoread.dto;

import likelion.dotoread.domain.Folder;

// FolderDTO.java
public record FolderDTO(Long id, String name) {

    public static FolderDTO from(Folder folder) {
        return new FolderDTO(folder.getId(), folder.getName());
    }
}