package de.take_weiland.mods.cameracraft.client.gui.memory.handler;

/**
 * @author Intektor
 */
public interface FolderHandler {

    /**
     * Returns the current selected file at the given folder ID
     * @param folderID
     * @return
     */
    ImageFile getSelectedFile(int folderID);

    /**
     * Returns the current rendered File at the given position
     * @param folderID
     * @param position
     * @return
     */
    ImageFile getFileAtPosition(int folderID, int position);

    /**
     * @param folderID
     * @return the starting file to render for the given folderID
     */
    int getStartFileID(int folderID);

    /**
     * Sets the start file for rendering
     * @param folderID
     * @param start
     */
    void setStartFileID(int folderID, int start);

    /**
     * Clears all files out of every Folder
     */
    void clearFiles();

    /**
     * Clears all files out of the folder
     * @param folderID
     */
    void clearFile(int folderID);

    /**
     * Updates all the folders
     */
    void updateFolders();

    /**
     * @return if it allows any changes by clicks
     * @param folderID
     */
    boolean allowsClicks(int folderID);

    /**
     * sets if it should allow to change things by clicks
     * @param allow
     * @param folderID
     */
    void allowClicks(int folderID, boolean allow);

    /**
     * sets if it should allow any clicks in any folder
     * @param allow
     */
    void allowClicks(boolean allow);
}
