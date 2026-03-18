resource "aws_vpc" "ticket_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name    = "ticket-platform-vpc"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_subnet" "public_subnet_a" {
  vpc_id                  = aws_vpc.ticket_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = true

  tags = {
    Name    = "ticket-public-subnet-a"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_subnet" "private_subnet_a" {
  vpc_id                  = aws_vpc.ticket_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = false

  tags = {
    Name    = "ticket-private-subnet-a"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
    Type    = "private"
  }
}

resource "aws_subnet" "public_subnet_c" {
  vpc_id                  = aws_vpc.ticket_vpc.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = true

  tags = {
    Name    = "ticket-public-subnet-c"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_subnet" "private_subnet_c" {
  vpc_id                  = aws_vpc.ticket_vpc.id
  cidr_block              = "10.0.4.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = false

  tags = {
    Name    = "ticket-private-subnet-c"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
    Type    = "private"
  }
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.ticket_vpc.id

  tags = {
    Name    = "ticket-igw"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.ticket_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name    = "ticket-public-rt"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.ticket_vpc.id

  tags = {
    Name    = "ticket-private-rt"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_route_table_association" "public_rt_assoc_a" {
  subnet_id      = aws_subnet.public_subnet_a.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "public_rt_assoc_c" {
  subnet_id      = aws_subnet.public_subnet_c.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "private_rt_assoc_a" {
  subnet_id      = aws_subnet.private_subnet_a.id
  route_table_id = aws_route_table.private_rt.id
}

resource "aws_route_table_association" "private_rt_assoc_c" {
  subnet_id      = aws_subnet.private_subnet_c.id
  route_table_id = aws_route_table.private_rt.id
}

resource "aws_security_group" "ec2_sg" {
  name        = "ticket-ec2-sg"
  description = "Security group for EC2 / k3s node"
  vpc_id      = aws_vpc.ticket_vpc.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["14.39.164.153/32"]
  }

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "k3s / app demo port (temporary)"
    from_port   = 6443
    to_port     = 6443
    protocol    = "tcp"
    cidr_blocks = ["14.39.164.153/32"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name    = "ticket-ec2-sg"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "ticket-rds-sg"
  description = "Security group for PostgreSQL RDS"
  vpc_id      = aws_vpc.ticket_vpc.id

  ingress {
    description     = "PostgreSQL from EC2 security group"
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ec2_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name    = "ticket-rds-sg"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_s3_bucket" "artifact_bucket" {
  bucket = "kwonjunyeol-ticket-platform-artifacts-20260316-01"

  tags = {
    Name    = "ticket-platform-artifacts"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_s3_bucket_versioning" "artifact_bucket_versioning" {
  bucket = aws_s3_bucket.artifact_bucket.id

  versioning_configuration {
    status = "Enabled"
  }
}

resource "aws_s3_bucket_public_access_block" "artifact_bucket_pab" {
  bucket = aws_s3_bucket.artifact_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}