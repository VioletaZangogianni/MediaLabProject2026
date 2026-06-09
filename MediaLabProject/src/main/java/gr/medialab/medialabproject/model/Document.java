package gr.medialab.medialabproject.model;

import gr.medialab.medialabproject.service.FileHandler;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a document in the medialab file system.
 * A document has a title, author, category, creation date and version number.
 * It supports creating, updating, opening, and deleting document files, while maintaining up to 3 previous versions.
 *
 * @author Violeta Zangogianni 03122213
 * @version 1.0
 */
public class Document extends FileSystemData {
    private String title;
    private String author;
    private Category category;
    private LocalDate dateCreated;
    private int version;

    /**
     * Constructs a new {@code Document} with the specified attributes.
     *
     * @param title       the title of the document
     * @param author      the author of the document
     * @param category    the category to which the document belongs
     * @param dateCreated the date the document was created
     * @param version     the initial version number of the document
     */
    public Document(String title, String author, Category category, LocalDate dateCreated, int version) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.dateCreated = dateCreated;
        this.version = version;
    }

    /**
     * Returns the name (title) of the document.
     *
     * @return the title of the document as a {@code String}
     */
    public String getName() {
        return title;
    }

    /**
     * Creates a new file for this document with the given content.
     * This method will only be called once, at the creation of each document.
     * The file will be created using the title and will be the first version of the document.
     *
     * @param content the text content to write into the document file
     */
    public void create(String content) {
        FileHandler.createFile(title, 1 , content);
    }

    /**
     * Updates the document by incrementing its version number by 1 and creating a new file
     * with the new provided content. If the version exceeds 3, the oldest version
     * (current version minus 3) is automatically deleted to maintain a maximum
     * of 3 stored versions.
     *
     * @param content the new text content to write into the updated document file
     */
    public void update(String content) {
        version++;
        FileHandler.createFile(title, version, content);

        if (version > 3)
            FileHandler.deleteFile(title, version - 3);
    }

    /**
     * Opens and returns the content of the document.
     *
     * @param showPrevious {@code true} to show both the current and the 2 previous versions of the document,
     *                     {@code false} to show only the current version
     * @return the content of the document file as a {@code String}
     */
    public String open(boolean showPrevious) {
        return FileHandler.openFile(title, version, showPrevious);
    }

    /**
     * Deletes all stored versions of the document from the file system.
     * Specifically, it removes up to the last 3 versions based on the current
     * version number (version, version-1, version-2).
     */
    public void delete() {
        if (version > 2)
            FileHandler.deleteFile(title, version - 2);
        if (version > 1)
            FileHandler.deleteFile(title, version - 1);
        FileHandler.deleteFile(title, version);
    }

    /**
     * Sets a new name (title) for the document.
     * This method is never called. It is only implemented for the parent class, FileSystemData.
     *
     * @param name the new title to assign to the document
     */
    public void setName(String name) {
        //this.title = name;
    }

    /**
     * Returns the category to which this document belongs.
     *
     * @return the {@link Category} of the document
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the current version number of the document.
     *
     * @return the version number as an {@code int}
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns the author of the document.
     *
     * @return the author's name as a {@code String}
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the date on which the document was originally created.
     *
     * @return the creation date as a {@link LocalDate}
     */
    public LocalDate getDate() {
        return dateCreated;
    }
}
