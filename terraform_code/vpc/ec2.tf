# This file contains the standalone EC2 instances for Jenkins, etc.

resource "aws_security_group" "demo_sg" {
  name        = "cicd-demo-server-sg" # Renamed
  description = "SSH and Jenkins Access"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "SSH access"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Jenkins port"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "cicd-demo-server-sg" # Renamed
  }
}

resource "aws_instance" "demo_servers" {
  for_each = toset(["jenkins-master", "build-slave", "ansible"])

  # Use the AMI we found in data.tf
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t2.micro"
  key_name      = var.key_pair_name # Uses "myKey" from variables.tf

  # Place these instances in a public subnet to be reachable
  subnet_id              = aws_subnet.public_1.id
  vpc_security_group_ids = [aws_security_group.demo_sg.id]

  tags = {
    Name = each.key
  }
}

