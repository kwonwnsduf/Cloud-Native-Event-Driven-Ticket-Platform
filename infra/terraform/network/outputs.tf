output "vpc_id" {
  value = aws_vpc.ticket_vpc.id
}

output "public_subnet_a_id" {
  value = aws_subnet.public_subnet_a.id
}

output "public_subnet_c_id" {
  value = aws_subnet.public_subnet_c.id
}

output "private_subnet_a_id" {
  value = aws_subnet.private_subnet_a.id
}

output "private_subnet_c_id" {
  value = aws_subnet.private_subnet_c.id
}

output "ec2_security_group_id" {
  value = aws_security_group.ec2_sg.id
}

output "rds_security_group_id" {
  value = aws_security_group.rds_sg.id
}

output "artifact_bucket_name" {
  value = aws_s3_bucket.artifact_bucket.bucket
}