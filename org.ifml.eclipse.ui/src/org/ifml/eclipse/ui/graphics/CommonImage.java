package org.ifml.eclipse.ui.graphics;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.google.common.base.Preconditions;

/**
 * General-purpose images.
 */
public enum CommonImage implements IImageProvider {

    /** Add */
    ADD(ISharedImages.IMG_OBJ_ADD),

    /** Application */
    APPLICATION,

    /** Application overlay */
    APPLICATION_OVR,

    /** Back */
    BACK(ISharedImages.IMG_TOOL_BACK),

    /** Blank (transparent) */
    BLANK,

    /** Bookmark */
    BOOKMARK(IDE.SharedImages.IMG_OBJS_BKMRK_TSK),

    /** Clear */
    CLEAR(ISharedImages.IMG_ETOOL_CLEAR),

    /** Collapse all */
    COLLAPSE_ALL(ISharedImages.IMG_ELCL_COLLAPSEALL),

    /** Completed overlay */
    COMPLETED_OVR,

    /** Copy */
    COPY(ISharedImages.IMG_TOOL_COPY),

    /** Cut */
    CUT(ISharedImages.IMG_TOOL_CUT),

    /** Database */
    DATABASE,

    /** Database overlay */
    DATABASE_OVR,

    /** Default overlay */
    DEFAULT_OVR,

    /** Delete */
    DELETE(ISharedImages.IMG_ETOOL_DELETE),

    /** Deny overlay */
    DENY_OVR,

    /** Download wizard banner */
    DOWNLOAD_WIZBAN,

    /** Edit */
    EDIT,

    /** Edit overlay */
    EDIT_OVR,

    /** Element */
    ELEMENT(ISharedImages.IMG_OBJ_ELEMENT),

    /** Error */
    ERROR(ISharedImages.IMG_OBJS_ERROR_TSK),

    /** Error overlay */
    ERROR_OVR(ISharedImages.IMG_DEC_FIELD_ERROR),

    /** File */
    FILE(ISharedImages.IMG_OBJ_FILE),

    /** Folder */
    FOLDER(ISharedImages.IMG_OBJ_FOLDER),

    /** Forward */
    FORWARD(ISharedImages.IMG_TOOL_FORWARD),

    /** Half-colored Star */
    HALF_STAR,

    /** Hash */
    HASH,

    /** Home */
    HOME(ISharedImages.IMG_ETOOL_HOME_NAV),

    /** Information */
    INFO(ISharedImages.IMG_OBJS_INFO_TSK),

    /** Install */
    INSTALL_TOOL,

    /** Key overlay */
    KEY_OVR,

    /** New */
    NEW,

    /** New overlay */
    NEW_OVR,

    /** Open marker */
    OPEN_MARKER(IDE.SharedImages.IMG_OPEN_MARKER),

    /** Paste */
    PASTE(ISharedImages.IMG_TOOL_PASTE),

    /** Pinned overlay */
    PINNED_OVR,

    /** Properties */
    PROPERTIES,

    /** Print */
    PRINT(ISharedImages.IMG_ETOOL_PRINT_EDIT),

    /** Project */
    PROJECT(IDE.SharedImages.IMG_OBJ_PROJECT),

    /** Project closed */
    PROJECT_CLOSED(IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED),

    /** Question */
    QUESTION,

    /** Question overlay */
    QUESTION_OVR,

    /** Redo */
    REDO(ISharedImages.IMG_TOOL_REDO),

    /** Refresh */
    REFRESH_TOOL,

    /** Remove */
    REMOVE(ISharedImages.IMG_ELCL_REMOVE),

    /** Remove all */
    REMOVE_ALL(ISharedImages.IMG_ELCL_REMOVEALL),

    /** Remove overlay */
    REMOVE_OVR,

    /** Run overlay */
    RUN_OVR,

    /** Save */
    SAVE(ISharedImages.IMG_ETOOL_SAVE_EDIT),

    /** Save all */
    SAVE_ALL(ISharedImages.IMG_ETOOL_SAVEALL_EDIT),

    /** Secure overlay */
    SECURE_OVR,

    /** SMTP server */
    SMTP_SERVER,

    /** Star */
    STAR,

    /** Stop */
    STOP(ISharedImages.IMG_ELCL_STOP),

    /** Synchronized */
    SYNC(ISharedImages.IMG_ELCL_SYNCED),

    /** Task */
    TASK(IDE.SharedImages.IMG_OBJS_TASK_TSK),

    /** Transparent 16x16 image */
    TRANSPARENT,

    /** Types */
    TYPES,

    /** Undo */
    UNDO(ISharedImages.IMG_TOOL_UNDO),

    /** Up */
    UP(ISharedImages.IMG_TOOL_UP),

    /** Up navigation */
    UP_NAVIGATION_TOOL,

    /** User */
    USER,

    /** Users */
    USERS,

    /** User overlay */
    USER_OVR,

    /** Warning */
    WARNING(ISharedImages.IMG_OBJS_WARN_TSK),

    /** Warning overlay */
    WARNING_OVR(ISharedImages.IMG_DEC_FIELD_WARNING),

    /** Web Service */
    WEB_SERVICE,

    /** XSD Provider */
    XSD_PROVIDER,

    /** XSD Resource */
    XSD_RESOURCE,

    ;

    private String symbolicName;

    CommonImage() {
    }

    CommonImage(String symbolicName) {
        Preconditions.checkNotNull(symbolicName);
        this.symbolicName = symbolicName;
    }

    /**
     * Returns the symbolic name associated with this image if the image references a workbench shared image. Returns {@code null}
     * otherwise.
     * 
     * @return the symbolic name or {@code null}.
     */
    public String getSharedSymbolicName() {
        return symbolicName;
    }

    @Override
    public Image get() {
        return (symbolicName != null) ? PlatformUI.getWorkbench().getSharedImages().getImage(symbolicName) : ImageProviders.get(this);
    }

    @Override
    public Image getDisabled() {
        return ImageProviders.getDisabled(this);
    }

    @Override
    public ImageDescriptor getDescriptor() {
        return (symbolicName != null) ? PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(symbolicName) : ImageProviders
                .getDescriptor(this);
    }

    @Override
    public ImageDescriptor getDisabledDescriptor() {
        return ImageProviders.getDisabledDescriptor(this);
    }

    @Override
    public Image get(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.get(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

    @Override
    public Image getDisabled(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.getDisabled(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

    @Override
    public ImageDescriptor getDescriptor(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.getDescriptor(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

    @Override
    public ImageDescriptor getDisabledDescriptor(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.getDisabledDescriptor(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

}
