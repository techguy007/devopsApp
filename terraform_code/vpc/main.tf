terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# --- EKS Modules Commented Out For Now ---
# We will re-enable these when you are ready to build the cluster.

# Module for the EKS-specific security groups
# module "sgs" {
#   source = "../sg_eks"
#   vpc_id = aws_vpc.main.id
# }

# Module for the EKS cluster
# module "eks" {
#   source = "../eks"
#   vpc_id = aws_vpc.main.id
#
#   # We are now passing the PUBLIC subnet IDs to the EKS module
#   subnet_ids = [aws_subnet.public_1.id, aws_subnet.public_2.id]
#
#   # Pass the security group from the sgs module
#   sg_ids = module.sgs.security_group_public
# }

