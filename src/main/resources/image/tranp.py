import os
from PIL import Image

def resize_images(source_folder, target_folder):
    # 如果目标文件夹不存在，则创建
    if not os.path.exists(target_folder):
        os.makedirs(target_folder)
    
    # 遍历源文件夹中的所有文件
    for filename in os.listdir(source_folder):
        if filename.endswith("_p.png"):
            # 构造完整的文件路径
            source_file = os.path.join(source_folder, filename)
            
            # 打开图像文件
            with Image.open(source_file) as img:
                # 检查图像的大小是否为500x380
                if img.size == (500, 380):
                    # 缩小图像
                    resized_img = img.resize((250, 190))
                    
                    # 构造新的文件名和目标文件路径
                    new_filename = filename.replace("_p.png", ".png")
                    target_file = os.path.join(target_folder, new_filename)
                    
                    # 保存缩小后的图像
                    resized_img.save(target_file)
                    print(f"Saved resized image to {target_file}")
                else:
                    print(f"Skipping {filename}: size is not 500x380")
        else:
            print(f"Skipping {filename}: does not match '_p.png' pattern")

# 使用示例
source_folder = "card"  # 替换为实际的源文件夹路径
target_folder = "card"  # 替换为实际的目标文件夹路径

resize_images(source_folder, target_folder)
