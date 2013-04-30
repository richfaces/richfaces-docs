# A sample Guardfile
# More info at https://github.com/guard/guard#readme

require 'erb'
require './asciidoc/src/main/ruby/RenderAsciidoc'

guard :shell, :all_on_start => true do
  watch /^([^\/]*)\/src\/.*\/(Component_Reference|Developer_Guide)\.asciidoc$/ do |m|
    source = m[0];
    target = m[1] + '/target/docbook/en-US/' + m[2] + '.html'
    puts "#{source} has changed"
    render_html(source, target)
  end
end

guard 'livereload' do
  watch(%r{target/.+\.(css|js|html)})
end
