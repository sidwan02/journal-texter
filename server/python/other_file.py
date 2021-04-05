import torch
from torch.utils.data import DataLoader, TensorDataset


# create Tensor datasets
train_data = TensorDataset(torch.from_numpy(
    train_x), torch.from_numpy(train_y))
valid_data = TensorDataset(torch.from_numpy(
    valid_x), torch.from_numpy(valid_y))
test_data = TensorDataset(torch.from_numpy(test_x), torch.from_numpy(test_y))
# dataloaders
batch_size = 50
# make sure to SHUFFLE your data
train_loader = DataLoader(train_data, shuffle=True, batch_size=batch_size)
valid_loader = DataLoader(valid_data, shuffle=True, batch_size=batch_size)
test_loader = DataLoader(test_data, shuffle=True, batch_size=batch_size)
