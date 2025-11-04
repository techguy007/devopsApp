# This finds the latest Ubuntu 22.04 LTS AMI for us.
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # This is Canonical's (Ubuntu's) official owner ID

  filter {
    name = "name"
    # Search for the 22.04 (Jammy) x86_64 server image
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

