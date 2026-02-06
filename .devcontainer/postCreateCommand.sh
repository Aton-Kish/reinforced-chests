#!/usr/bin/bash

set -eux

sudo apt-get update

# https://lefthook.dev/installation/deb.html
curl -1sLf 'https://dl.cloudsmith.io/public/evilmartians/lefthook/setup.deb.sh' | sudo -E bash

sudo apt-get install -y \
    bash-completion \
    libgl1-mesa-dev \
    lefthook

# https://docs.astral.sh/uv/getting-started/installation/
curl -LsSf https://astral.sh/uv/install.sh | sh
echo 'eval "$(uv generate-shell-completion bash)"' >> ~/.bashrc
echo 'eval "$(uvx --generate-shell-completion bash)"' >> ~/.bashrc

lefthook install
