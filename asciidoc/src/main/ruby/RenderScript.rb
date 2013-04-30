#!/usr/bin/ruby

require_relative 'RenderAsciidoc'


script_dir = File.dirname(__FILE__)
$project_dir = "#{script_dir}/../../../.."

def render(basename)
  puts "Generating docbook xml for #{basename}"
  source_dir = "#{$project_dir}/#{basename}/src/main/docbook/en-US"
  base_ad = "#{source_dir}/#{basename}.asciidoc"
  base_xml = "#{source_dir}/#{basename}.asciidoc.xml"
  render_docbook(base_ad, base_xml);
  tidy(base_xml)
end

def tidy(base_xml)
  cmd = "xmllint --format #{base_xml} -o #{base_xml}"
  result = system ( cmd )
  if (! result)
    puts 'xmllint command failed'
  end
end


render('Developer_Guide')
render('Component_Reference')