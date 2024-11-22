package likelion.dotoread.web.dto.FolderDto;

import likelion.dotoread.domain.Folder;

public record FolderDTO(Long id, String name) {

    public static FolderDTO from(Folder folder) {
        return new FolderDTO(folder.getId(), folder.getName());
    }
}