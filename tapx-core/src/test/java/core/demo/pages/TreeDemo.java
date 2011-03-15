package core.demo.pages;

import java.io.File;

import org.apache.tapestry5.annotations.InjectComponent;

import com.demo.data.FileSystemTreeModel;
import com.howardlewisship.tapx.core.TreeModel;
import com.howardlewisship.tapx.core.components.Tree;

public class TreeDemo
{
    @InjectComponent
    private Tree fs;

    public TreeModel<File> getFileSystemTreeModel()
    {
        return new FileSystemTreeModel();
    }

    void onActionFromClear()
    {
        fs.clearExpansions();
    }
}
