output "vpc_id" {
  description = "The ID of the main VPC"
  value       = aws_vpc.main.id
}

output "public_subnet_ids" {
  description = "List of IDs for the public subnets (for EKS)"
  value = [
    aws_subnet.public_1.id,
    aws_subnet.public_2.id
  ]
}

output "demo_server_ips" {
  description = "Public IP addresses of the demo servers"
  value = {
    for k, v in aws_instance.demo_servers : k => v.public_ip
  }
}

