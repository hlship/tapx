package core.demo.pages;

import java.io.File;
import java.util.List;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;

import com.howardlewisship.tapx.core.TreeModel;
import com.howardlewisship.tapx.core.TreeNode;

public class TreeDemo
{
    public TreeModel<File> getFileSystemTreeModel()
    {
        return new TreeModel<File>()
        {
            final Mapper<File, TreeNode<File>> fileToTreeNode = new Mapper<File, TreeNode<File>>()
            {
                @Override
                public TreeNode<File> map(File value)
                {
                    return new FileTreeNode(value);
                }
            };

            class FileTreeNode implements TreeNode<File>
            {
                private final File file;

                FileTreeNode(File file)
                {
                    this.file = file;
                }

                @Override
                public String getId()
                {
                    return file.getPath();
                }

                @Override
                public File getValue()
                {
                    return file;
                }

                @Override
                public boolean isLeaf()
                {
                    return file.isFile();
                }

                @Override
                public boolean getHasChildren()
                {
                    return file.list().length > 0;
                }

                @Override
                public List<TreeNode<File>> getChildren()
                {
                    return F.flow(file.listFiles()).map(fileToTreeNode).toList();
                }

                @Override
                public String getLabel()
                {
                    return file.getName();
                }

            }

            @Override
            public List<TreeNode<File>> getRootNodes()
            {

                return F.flow(new File(".").listFiles()).map(fileToTreeNode).toList();
            }

            @Override
            public TreeNode<File> getById(String id)
            {
                return new FileTreeNode(new File(id));
            }

        };
    }
}
