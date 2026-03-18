terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_iam_role" "ec2_app_role" {
  name = "ticket-platform-ec2-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name    = "ticket-platform-ec2-role"
    Project = "cloud-native-ticket-platform"
    Env     = "dev"
  }
}

resource "aws_iam_policy" "ec2_app_policy" {
  name        = "ticket-platform-ec2-policy"
  description = "Policy for EC2 to access S3 and SSM Parameter Store"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "S3BucketList"
        Effect = "Allow"
        Action = [
          "s3:ListBucket"
        ]
        Resource = [
          "arn:aws:s3:::kwonjunyeol-ticket-platform-artifacts-20260316-01"
        ]
      },
      {
        Sid    = "S3ObjectAccess"
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:PutObject"
        ]
        Resource = [
          "arn:aws:s3:::kwonjunyeol-ticket-platform-artifacts-20260316-01/*"
        ]
      },
      {
        Sid    = "SSMParameterRead"
        Effect = "Allow"
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters",
          "ssm:GetParametersByPath"
        ]
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ec2_app_policy_attach" {
  role       = aws_iam_role.ec2_app_role.name
  policy_arn = aws_iam_policy.ec2_app_policy.arn
}

resource "aws_iam_instance_profile" "ec2_instance_profile" {
  name = "ticket-platform-ec2-instance-profile"
  role = aws_iam_role.ec2_app_role.name
}