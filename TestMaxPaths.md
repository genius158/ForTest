```
/**
 * 二叉树，返回最大深度叶子结点的路径
 */
public class TestMaxPaths {

    class TreeNode {
        int val;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode left;
        TreeNode right;
    }


    @Test
    public void addition_isCorrect() {
        TreeNode root = new TreeNode(1);
        TreeNode leftLeft = new TreeNode(2);
        leftLeft.left = new TreeNode(4);
        root.left = leftLeft;
        root.right = new TreeNode(3);
        maxPaths(root);
    }

    /**
     * 单前访问到的路径
     */
    private final LinkedList<TreeNode> path = new LinkedList<>();
    /**
     * 最大路径list (可能多个)
     */
    private final ArrayList<LinkedList<TreeNode>> maxPath = new ArrayList<>();

    public ArrayList<LinkedList<TreeNode>> maxPaths(TreeNode root) {
        // 保证maxPath.get(0) 不为空
        maxPath.add(new LinkedList<>(path));
        dfs(root);
        return maxPath;
    }

    /**
     * 因为要返回路径 这里用深度遍历
     */
    void dfs(TreeNode root) {
        if (root == null) return;
        path.add(root);
        if (root.left == null && root.right == null) {
            // 获取最大路径记录
            LinkedList<TreeNode> tmp = maxPath.get(0);
            if (path.size() > tmp.size()) {
                // 当前叶子结点路径大于之前记录的路径
                // 清空最大路径和
                maxPath.clear();

                // 添加上当前叶子路径
                maxPath.add(new LinkedList<>(path));
            } else if (path.size() == tmp.size()) {
                // 路径相等的请求下直接添加该路家
                maxPath.add(new LinkedList<>(path));
            }
        }
        dfs(root.left);
        dfs(root.right);
        // 回到上一层 path回退
        path.removeLast();
    }

}

```
